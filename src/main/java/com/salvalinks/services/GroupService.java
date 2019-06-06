package com.salvalinks.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	Util util;
	
	public Boolean groupExists(String email, String name) {
		User user = this.userService.getByEmail(email);
		Boolean retorno = false;
		if(user.containsGroup(name) != null)
			retorno = true;
		return retorno;
	}
	
	public Group addGroup(String email, String name) throws Exception {
		User user = this.userService.getByEmail(email);
		if(this.groupExists(email, name))
			throw new Exception("Já existe grupo com esse nome!");
		
		String id = this.util.encrypt(name);
		Group group = new Group(id,name);
		user.getGroups().add(group);
		this.userService.saveUser(user);
		return group;
	}
	
	public String addLinkToGroup(String email, String nameGroup, String idLink) {
		User user = this.userService.getByEmail(email);
		Link link = user.getLinkById(idLink);
		Group group = user.containsGroup(nameGroup);
		group.getLinks().add(idLink); // id do link no grupo
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

}
