package com.salvalinks.tests.unidade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.Group;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupTest {

	private Group group;
	
	@Before
	public void setUp() {
		this.group = new Group("teste", "teste");
	}
	
	@Test
	public void testEquals() {
		Group group = new Group("teste", "teste");
		Assert.assertTrue("Os grupos deveriam ser iguais.", group.equals(this.group));
	}
	
	@Test
	public void testNotEquals() {
		Group group = new Group("teste", "teste12323");
		Assert.assertFalse("Os grupos deveriam ser diferentes.", group.equals(this.group));
	}
}
