package com.salvalinks.tests.unidade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.Group;
import com.salvalinks.models.Link;
import com.salvalinks.models.Notification;
import com.salvalinks.models.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

	private User user;
	
	@Before
	public void setUp() {
		this.user = new User("Teste", "test@gmail.com", "teste123", "3GHANF");
		this.user.setLinks(new HashSet<Link>());
		this.user.setGroups(new HashSet<Group>());
		this.user.setNotifications(new HashSet<Notification>());
	}
	
	@Test
	public void testGetLinkById() {
		Link link = new Link("teste", "https://google.com.br", "high", "text", "aapsdojsfpo");
		this.user.getLinks().add(link);
		Link searched = this.user.getLinkById("aapsdojsfpo");
		Link found = link;
		Assert.assertTrue("Os links deveriam ser iguais.", searched.equals(found));
	}
	
	@Test
	public void testContainsGroup() {
		Group group = new Group("grupo", "gruponome");
		this.user.getGroups().add(group);
		Group searched = this.user.containsGroup("gruponome");
		Group found = group;
		Assert.assertTrue("Os grupos deveriam ser iguais", searched.equals(found));
	}
	
	@Test
	public void testContainsLink() {
		Link link = new Link("teste", "https://google.com.br", "high", "text", "aapsdojsfpo");
		this.user.getLinks().add(link);
		Boolean found = this.user.containsLink("https://google.com.br");
		Assert.assertTrue("O link deveria ter sido encontrado.", found);
	}
	
	@Test
	public void testRemoveLink() {
		Link link = new Link("teste", "https://google.com.br", "high", "text", "aapsdojsfpo");
		this.user.getLinks().add(link);
		Link removed = this.user.removeLink("aapsdojsfpo");
		Assert.assertTrue("O link deveria ser igual, já que seria removido.", link.equals(removed));
	}
	
	@Test
	public void testRemoveNotification() throws Exception {
		Link link = new Link("teste", "https://google.com.br", "high", "text", "aapsdojsfpo");
		this.user.getLinks().add(link);
		String time = "10/06/2019 12:00";
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time);
		Notification notif = new Notification("dsahdaduhda", "https://google.com.br", date);
		this.user.getNotifications().add(notif);
		Notification returned = this.user.removeNotification("dsahdaduhda");
		Assert.assertTrue("As notificações deveriam ser iguais.", returned.equals(notif));
	}
	
}
