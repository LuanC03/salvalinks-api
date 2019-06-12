package com.salvalinks.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.salvalinks.models.Group;
import com.salvalinks.models.Link;
import com.salvalinks.models.User;

@Service
public class GroupService {

	@Autowired
	UserService userService;

	@Autowired
	LinkService linkService;
	
	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	Util util;

	public Boolean groupExists(String email, String name) {
		User user = this.userService.getByEmail(email);
		Boolean retorno = false;
		if (user.containsGroup(name) != null)
			retorno = true;
		return retorno;
	}

	public Group addGroup(String email, String name) throws Exception {
		User user = this.userService.getByEmail(email);
		if (user.getGroups() == null)
			user.setGroups(new HashSet<>());

		if (this.groupExists(email, name))
			throw new Exception("Já existe grupo com esse nome!");

		String id = this.util.encrypt(name);
		Group group = new Group(id, name);
		user.getGroups().add(group);
		this.userService.saveUser(user);
		return group;
	}

	public String addLinkToGroup(String email, String nameGroup, String idLink) {
		User user = this.userService.getByEmail(email);
		Link link = user.getLinkById(idLink);
		Group group = user.containsGroup(nameGroup);
		group.getLinks().add(idLink); // id do link no grupos
		link.setGroup(nameGroup); // seta atrr group do link
		user.getGroups().add(group); // add o grupo atualizado PUT
		user.getLinks().add(link); // add link atualizado PUT
		this.userService.saveUser(user); // salva user atualizado PUT
		return idLink;
	}

	public String removeLinkFromGroup(String email, String nameGroup, String idLink) {
		User user = this.userService.getByEmail(email);
		Link link = user.getLinkById(idLink);
		link.setGroup("none"); // seta atrr group para null
		user.getLinks().add(link); // add o link atualizado PUT
		user.removeFromGroup(nameGroup, idLink); // remove link do grupo
		this.userService.saveUser(user); // Salva user atualizado
		return idLink;
	}

	public Set<Link> getLinksFromGroup(String email, String name) {
		User user = this.userService.getByEmail(email);
		Set<Link> retorno = new HashSet<>();
		Iterator<Link> iterator = user.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getGroup().equals(name))
				retorno.add(link);
		}
		return retorno;
	}

	public void deleteGroup(String email, String name) {
		User user = this.userService.getByEmail(email);
		Group group = user.containsGroup(name);
		Iterator<Link> iterator = this.getLinksFromGroup(email, name).iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			user.removeFromGroup(name, link.getId());
		}
		user.getGroups().remove(group);
		this.userService.saveUser(user);
	}

	public HttpServletResponse shareLinks(String email, String name, HttpServletResponse response) {
		User user = this.userService.getByEmail(email);
		Document document = new Document();
		Set<Link> links = this.getLinksFromGroup(email, name);
		response.setContentType("application/pdf");

		try { 
			PdfWriter.getInstance(document, response.getOutputStream());
			//PdfWriter.getInstance(document, new FileOutputStream("salvalinks.herokuapp.com/"+user.getValidationCode()+"/pdf_Test.pdf"));
			document.open();
			
			// LOGO // FALTA COLOCAR IMAGEM ON EM ALGUM LINK
			String imageFile = "link"; 
			Image data = Image.getInstance(imageFile);
			data.scalePercent(30, 30);
			data.setAlignment(Element.ALIGN_CENTER);
			document.add(data);
			// TITULO
			document.add(new Paragraph("     "+ name, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
			document.add(new Paragraph(" "));
			//RODAPE
			Paragraph rodape = new Paragraph("© Copyright 2019, SalvaLinks");
			rodape.setAlignment(Element.ALIGN_BOTTOM);
			rodape.setAlignment(Element.ALIGN_CENTER);
			
			Iterator iterator = links.iterator();
			while (iterator.hasNext()) {
				Link link = (Link) iterator.next();
				document.add(new Paragraph("* " + link.getName()));
				document.add(new Paragraph("  " + link.getHref(), FontFactory.getFont(FontFactory.COURIER, 12)));
			}
			
			document.add(new Paragraph(" "));
			document.add(rodape);
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		} finally {
			document.close();
		}
	
		return response;
	}

	// public String renameGroup(String email, String name, String newName) {
	// User user = this.userService.getByEmail(email);
	// Group group = user.containsGroup(name);
	// Iterator<Link> iterator = this.getLinksFromGroup(email, name).iterator();
	// while (iterator.hasNext()) {
	// Link link = (Link) iterator.next();
	// this.addLinkToGroup(email, nameGroup, idLink)
	// }
	// user.getGroups().remove(group);
	// group.setName(newName);
	// user.getGroups().add(group);
	//
	// }

}
