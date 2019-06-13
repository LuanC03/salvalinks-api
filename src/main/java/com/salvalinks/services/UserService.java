package com.salvalinks.services;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.salvalinks.models.User;
import com.salvalinks.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.Claims;

@Service
public class UserService {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
	public final String apikey =  "projetoessalvalinksandreadrianoaislandanieljonasfernandolucasthallysonwellington";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Util util;

	@Autowired
	private EmailSenderService emailSenderService;

	public List<User> getAll() throws IOException {
		return this.userRepository.findAll();
	}

	public User getByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}
	
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	public void deleteAll() {
		this.userRepository.deleteAll();
	}

	private Claims parseJWT(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apikey)).parseClaimsJws(jwt).getBody();
		return claims;
	}

	// -------------------------------------------------
	// Metodos de validacao e verificacao:
	// -------------------------------------------------

	public String checkJWT(String bearer) throws Exception {
		if (bearer == null || !bearer.substring(0, 6).equals("Bearer"))
			throw new Exception("Token não presente ou inválido");
		
		String token = bearer.substring(7);
		Claims data = parseJWT(token);
		Date instant = new Date(System.currentTimeMillis());
		
		if (data.getExpiration().before(instant))
			throw new Exception("Token expirado");
		
		String email = data.getSubject();
		
		return email;
	}

	private String createJWT(User user) {
		@SuppressWarnings("deprecation")
		String token = Jwts.builder().setSubject(user.getEmail()).setId(user.getId()).signWith(SignatureAlgorithm.HS256,apikey)
				.setExpiration(new Date(System.currentTimeMillis() + (60000 * 360))).compact();

		return token;
	}

	public boolean validateEmail(String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private void validateUser(User user, String password) throws Exception {
		if (user == null)
			throw new Exception("Email não cadastrado!");

		if (!user.isEnabled())
			throw new Exception("Email ainda não confirmado!");

		if (!util.verifyPassword(user.getPassword(), password))
			throw new Exception("Senha incorreta!");
	}
	
	public void sendLinkToRedefine(String email) {
		User user = this.getByEmail(email);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Redefinir Senha");
		mailMessage.setFrom("SalvaLinks");
		mailMessage.setText("Olá " + user.getName()
				+ ", para redefinir sua senha em SalvaLinks, use o link a seguir: salvalinks.herokuapp.com/redefinePassword?token="
				+ user.getValidationCode() + "&email=" + user.getEmail());

		emailSenderService.sendEmail(mailMessage);
	}
	
	public void validateTokenRedefine(String email, String code) throws Exception {
		User user = getByEmail(email);
		if (!user.getValidationCode().equals(code))
			throw new Exception("Código inválido!");
		
	}
	
	public void redefinePassword(String code, String email, String password, String newPassword) throws Exception {
		User user = getByEmail(email);
		if (!user.getValidationCode().equals(code))
			throw new Exception("Código inválido!");

		else if (!password.equals(newPassword))
			throw new Exception("As senhas não coincidem");

		user.setValidationCode(this.getCode());
		user.setPassword(this.util.encrypt(password));
		this.saveUser(user);

	}

	public Boolean validation(String email, String code) throws Exception {
		User user = getByEmail(email);
		if (!user.getValidationCode().equals(code))
			throw new Exception("Código inválido!");

		user.setEnabled(true);
		this.saveUser(user);
		return true;

	}

	// ---------------------------------------------------
	// Metodos funcionais do sistema:
	// ---------------------------------------------------
	public User registerUser(User user) throws Exception {
		if (!validateEmail(user.getEmail()))
			throw new Exception("Formato de email inválido!");

		if (!util.validatePassword(user.getPassword()))
			throw new Exception("Senha muito curta, mínimo 6 caracteres!");

		User check = this.getByEmail(user.getEmail());

		if (check != null)
			throw new Exception("Email já cadastrado, mandamos um link, checa lá ;)");

		String code = getCode();
		String encryptedPassword = util.encrypt(user.getPassword());
		User newUser = new User(user.getName(), user.getEmail().toLowerCase(), encryptedPassword, code);
		this.saveUser(newUser);
		sendConfirmationEmail(newUser, code);
		return newUser;
	}

	private void sendConfirmationEmail(User user, String code) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Quase lá :)");
		mailMessage.setFrom("SalvaLinks");
		mailMessage.setText("Olá " + user.getName()
				+ ", para confirmar seu cadastro em SalvaLinks, use o link a seguir: salvalinks.herokuapp.com/confirm-account?token="
				+ code + "&email=" + user.getEmail());

		emailSenderService.sendEmail(mailMessage);
	}

	private String getCode() {
		String code = "";
		Random r = new Random();
		code = Integer.toString(Math.abs(r.nextInt()), 36).substring(0, 6);
		return code.toUpperCase();
	}

	public String logar(String email, String password) throws Exception {
		User user = this.getByEmail(email.toLowerCase());
		validateUser(user, password);
		String token = createJWT(user);
		System.out.println(token);
		return token;
	}
	
	public void renameUser(String email, String newName) {
		User user = this.getByEmail(email);
		user.setName(newName);
		this.saveUser(user);
	}

}
