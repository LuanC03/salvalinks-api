package com.salvalinks.tests.integracao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.Link;
import com.salvalinks.models.User;
import com.salvalinks.services.LinkService;
import com.salvalinks.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkServiceTest {

	@Autowired
	private LinkService linkService;

	@Autowired
	private UserService userService;

	private User user;

	@Before
	public void setUp() throws Exception {
		this.userService.deleteAll();
		user = new User("TESTE", "TESTE", "TESTE", "TESTE");
		this.userService.saveUser(user);
		linkService.addLink(user.getEmail(), "youtube", "youtube.com", "teste");
		linkService.addLink(user.getEmail(), "google", "google.com", "teste");
		linkService.addLink(user.getEmail(), "github", "github.com", "teste");
	}

	@Test
	public void testGetLinkById() throws Exception {
		assertNotNull(this.linkService.getLinkById(user.getEmail(), "eW91dHViZS5jb20="));
	}

	@Test
	public void testGetLinkByUrl() throws Exception {
		assertNotNull(this.linkService.getLinkByUrl(user.getEmail(), "youtube.com"));
	}

	@Test
	public void testGetLinks() throws Exception {
		// Link link = new
		// Link("youtube","youtube.com","teste","text/html","eW91dHViZS5jb20=");
		Assert.assertTrue(this.linkService.getLinks(user.getEmail()).size() == 3);
	}

	@Test
	public void testAddLink() throws Exception {
		linkService.addLink(user.getEmail(), "twitter", "twitter.com", "teste");
		Assert.assertTrue(this.linkService.getLinks(user.getEmail()).size() == 4);
	}

	@Test
	public void testRemoveLink() throws Exception {
		Assert.assertTrue(this.linkService.getLinks(user.getEmail()).size() == 3);
		linkService.removeLink(user.getEmail(), "youtube.com");
		Assert.assertTrue(this.linkService.getLinks(user.getEmail()).size() == 2);
	}

	@Test
	public void testListByName() throws Exception {
		List<Link> list = this.linkService.listByName(user.getEmail());
		Assert.assertTrue(list.get(0).getName().equals("github"));
		Assert.assertTrue(list.get(1).getName().equals("google"));
		Assert.assertTrue(list.get(2).getName().equals("youtube"));
	}

	@Test
	public void testRenameLink() throws Exception {
		Assert.assertTrue(this.linkService.getLinkByUrl(user.getEmail(), "youtube.com").getName().equals("youtube"));
		this.linkService.renameLink(user.getEmail(), "youtube.com", "yt");
		Assert.assertTrue(this.linkService.getLinkByUrl(user.getEmail(), "youtube.com").getName().equals("yt"));
	}
	
	@Test
	public void testGetTitle() throws Exception {
		Assert.assertTrue(this.linkService.getTitle("http://google.com").equals("Google"));
	}

	@Test
	public void testGetType() throws Exception {
		Assert.assertTrue(this.linkService.getType("http://youtube.com").equals("text/html"));
	}
	
}
