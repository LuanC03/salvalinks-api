package com.salvalinks.tests.integracao;

import java.awt.List;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.User;
import com.salvalinks.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	private User user;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testGetBy() throws IOException {
		this.userService.deleteAll();
		this.user = new User("lucas","TESTE@TESTE","TESTE123","TESTE");
		User userSaved = this.userService.saveUser(this.user);
		User found = userService.getByEmail(user.getEmail());
		System.out.println(found.equals(userSaved));
		System.out.println(userSaved.toString());
		System.out.println(found.toString());
        //Assert.assertTrue("O user deveria ter sido salvo.", found.equals(user));
        Assert.assertEquals("Os users deveriam ser iguais", found, this.user);
	}
	
	
	
}
