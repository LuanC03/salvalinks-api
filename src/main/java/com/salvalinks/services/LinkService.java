package com.salvalinks.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvalinks.models.Link;
import com.salvalinks.models.User;

@Service
public class LinkService {

	@Autowired
	UserService userService;

	@Autowired
	Util util;

	private void checkIfUserHasLinks(User user) throws Exception {
		if (user.getLinks().isEmpty())
			throw new Exception("Usuário não possui links");
	}

	private void checkIfLinkIsNotAdded(User user, String url) throws Exception {
		if (user.containsLink(url))
			throw new Exception("Link já adicionado!");
	}

	private void checkIfLinkIsAlreadyAdded(User user, String url) throws Exception {
		if (!user.containsLink(url))
			throw new Exception("Link não encontrado!");
	}

	public Link getLinkById(String email, String id) {
		User user = this.userService.getByEmail(email);
		Link retorno = null;
		Set<Link> set = user.getLinks();
		Iterator<Link> iterator = set.iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getId().equals(id))
				retorno = link;
		}

		return retorno;
	}

	public Link getLinkByUrl(String email, String url) {
		User user = this.userService.getByEmail(email);
		Link retorno = null;
		Set<Link> set = user.getLinks();
		Iterator<Link> iterator = set.iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getHref().equals(url))
				retorno = link;
		}

		return retorno;
	}

	public Set<Link> getLinks(String email) throws Exception {
		User user = this.userService.getByEmail(email);
		Set<Link> retorno = null;
		if (user.getLinks().isEmpty())
			retorno = new Hashset<>();
		else
			retorno = user.getLinks();
		return retorno;
	}

	public Link addLink(String email, String name, String href, String importance) throws Exception {
		User user = this.userService.getByEmail(email);
		checkIfLinkIsNotAdded(user, href);

		String nameLink = name;
		if (name.equals(""))
			nameLink = getTitle(href);

		String type = getType(href);
		String id = this.util.encrypt(href);
		Link link = new Link(nameLink, href, importance, type, id);
		user.getLinks().add(link);
		this.userService.saveUser(user);
		return link;
	}

	public Link removeLink(String email, String url) throws Exception {
		User user = this.userService.getByEmail(email);
		checkIfLinkIsAlreadyAdded(user, url);
		Link idLink = this.getLinkByUrl(email, url);
		Link retorno = user.removeLink(idLink.getId());
		this.userService.saveUser(user);
		return retorno;

	}

	public List<Link> listByName(String email) throws Exception {
		User user = this.userService.getByEmail(email);
		checkIfUserHasLinks(user);
		return user.orderByNome();
	}

	public List<Link> listByDate(String email) throws Exception {
		User user = this.userService.getByEmail(email);
		checkIfUserHasLinks(user);
		return user.orderByDate();
	}

	public Link renameLink(String email, String url, String newName) throws Exception {
		User user = this.userService.getByEmail(email);
		checkIfLinkIsAlreadyAdded(user, url);
		Link retorno = null;
		Iterator<Link> iterator = user.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getHref().equals(url)) {
				link.setName(newName);
				user.getLinks().add(link);
				this.userService.saveUser(user);
				retorno = link;
			}
		}
		return retorno;
	}

	public String getTitle(String url) throws IOException {
		String retorno = null;
		if (url.endsWith("pdf"))
			retorno = url;

		else {
			String href = urlCheck(url);
			Document document = Jsoup.connect(href).get();
			retorno = document.getElementsByTag("title").get(0).text();
		}
		return retorno;
	}

	public String getType(String href) throws IOException {
		String contentType = null;
		System.out.println(href);
		if (href.endsWith("pdf"))
			contentType = "pdf";

		else {
			String hrefChecked = urlCheck(href);
			URL url = new URL(hrefChecked);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.connect();
			contentType = connection.getContentType();
		}
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

}
