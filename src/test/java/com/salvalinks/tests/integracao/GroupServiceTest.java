package com.salvalinks.tests.integracao;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.User;
import com.salvalinks.services.GroupService;
import com.salvalinks.services.LinkService;
import com.salvalinks.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupServiceTest {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LinkService linkService;
	
	private User user;
	
	@Before
	public void setUp() throws Exception {
		this.userService.deleteAll();
		user = new User("TESTE", "TESTE", "TESTE", "TESTE");
		this.userService.saveUser(user);
		this.groupService.addGroup(user.getEmail(), "grupo");
		linkService.addLink(user.getEmail(), "youtube", "youtube.com", "teste");
		linkService.addLink(user.getEmail(), "google", "google.com", "teste");
		linkService.addLink(user.getEmail(), "github", "github.com", "teste");
	}
	
	@Test
	public void testGroupExists() throws Exception {
		Assert.assertTrue(this.groupService.groupExists(user.getEmail(), "grupo"));
	}
	
	@Test
	public void testAddGroup() throws Exception {
		Assert.assertTrue(this.userService.getByEmail(user.getEmail()).getGroups().size() == 1);
		this.groupService.addGroup(user.getEmail(), "groupteste");
		Assert.assertTrue(this.userService.getByEmail(user.getEmail()).getGroups().size() == 2);
	}
	
	@Test
	public void testAddLinkToGroup() throws Exception {
		this.groupService.addLinkToGroup(user.getEmail(), "grupo", "eW91dHViZS5jb20=");
		Assert.assertTrue(this.groupService.getLinksFromGroup(user.getEmail(), "grupo").size() == 1);
		Assert.assertTrue(this.userService.getByEmail(user.getEmail()).getLinkById("eW91dHViZS5jb20=").getGroup().equals("grupo"));
	}
	
	@Test
	public void testRemoveLinkToGroup() throws Exception {
		this.groupService.addLinkToGroup(user.getEmail(), "grupo", "eW91dHViZS5jb20=");
		Assert.assertTrue(this.groupService.getLinksFromGroup(user.getEmail(), "grupo").size() == 1);
		Assert.assertTrue(this.userService.getByEmail(user.getEmail()).getLinkById("eW91dHViZS5jb20=").getGroup().equals("grupo"));
		this.groupService.removeLinkFromGroup(user.getEmail(), "grupo", "eW91dHViZS5jb20=");
		Assert.assertTrue(this.groupService.getLinksFromGroup(user.getEmail(), "grupo").size() == 0);
		Assert.assertTrue(this.userService.getByEmail(user.getEmail()).getLinkById("eW91dHViZS5jb20=").getGroup().equals("none"));
	}

	@Test
	public void testGetLinksFromGroup() throws Exception {
		Assert.assertTrue(this.groupService.getLinksFromGroup(user.getEmail(), "grupo").size() == 0);
		this.groupService.addLinkToGroup(user.getEmail(), "grupo", "eW91dHViZS5jb20=");
		Assert.assertTrue(this.groupService.getLinksFromGroup(user.getEmail(), "grupo").size() == 1);	}
	
	
}
