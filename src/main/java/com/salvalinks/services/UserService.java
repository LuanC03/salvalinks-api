package com.salvalinks.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.salvalinks.models.Link;
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

	private void checkIfUserHasLinks(User user) throws Exception {
		if (user.getLinks().isEmpty())
			throw new Exception("Usuário não possui nenhum link!");
	}

	private void checkIfLinkIsNotAdded(User user, String url) throws Exception {
		if (user.containsLink(url))
			throw new Exception("Link já adicionado!");
	}

	private void checkIfLinkIsAlreadyAdded(User user, String url) throws Exception {
		if (!user.containsLink(url))
			throw new Exception("Link não encontrado!");
	}

	private void validateUser(User user, String password) throws Exception {
		if (user == null)
			throw new Exception("Email não cadastrado!");

		if (!user.isEnabled())
			throw new Exception("Email ainda não confirmado!");

		if (!util.verifyPassword(user.getPassword(), password))
			throw new Exception("Senha incorreta!");
	}

	public Boolean validation(String email, String code) throws Exception {
		User user = getByEmail(email);
		if (!user.getValidationCode().equals(code))
			throw new Exception("Código inválido!");

		user.setEnabled(true);
		this.userRepository.save(user);
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
		User newUser = new User(user.getName(), user.getEmail(), encryptedPassword, code);
		this.userRepository.save(newUser);
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
		User user = this.getByEmail(email);
		validateUser(user, password);
		String token = createJWT(user);
		return token;
	}

	private String getTitle(String url) throws IOException {
		String href = urlCheck(url);
		Document document = Jsoup.connect(href).get();
		String retorno = document.getElementsByTag("title").get(0).text();
		return retorno;
	}

	private String getType(String href) throws IOException {
		String hrefChecked = urlCheck(href);
		URL url = new URL(hrefChecked);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");
		connection.connect();
		String contentType = connection.getContentType();
		return contentType;
	}

	private String urlCheck(String url) {
		String href = "";
		if (url.substring(0, 7).equals("http://"))
			href = url;

		else if (url.substring(0, 8).equals("https://"))
			href = "http://" + url.substring(8, url.length());

		else
			href = "http://" + url;

		return href;
	}

	public Set<Link> getLinks(String email) throws Exception {
		User user = getByEmail(email);
		checkIfUserHasLinks(user);
		return user.getLinks();
	}

	public Link addLink(String email, String name, String href, String importance) throws Exception {
		User user = getByEmail(email);
		checkIfLinkIsNotAdded(user, href);

		String nameLink = name;
		if (name.equals(""))
			nameLink = getTitle(href);

		String type = getType(href);
		Link link = new Link(nameLink, href, importance, type);
		user.getLinks().add(link);
		this.userRepository.save(user);
		return link;
	}

	public Link removeLink(String email, String url) throws Exception {
		User user = getByEmail(email);
		checkIfLinkIsAlreadyAdded(user, url);
		Link retorno = user.removeLink(url);
		this.userRepository.save(user);
		return retorno;

	}

	public List<Link> listByName(String email) throws Exception {
		User user = getByEmail(email);
		checkIfUserHasLinks(user);
		return user.orderByNome();
	}

	public List<Link> listByDate(String email) throws Exception {
		User user = getByEmail(email);
		checkIfUserHasLinks(user);
		return user.orderByDate();
	}

	public Link renameLink(String email, String url, String newName) throws Exception {
		User user = getByEmail(email);
		checkIfLinkIsAlreadyAdded(user, url);
		Link retorno = null;
		Iterator<Link> iterator = user.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getHref().equals(url)) {
				link.setName(newName);
				user.getLinks().add(link);
				this.userRepository.save(user);
				retorno = link;
			}
		}
		return retorno;
	}

}
