package com.salvalinks.tests.unidade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.Link;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkTest {

	private Link link;
	
	@Before
	public void setUp() {
		this.link = new Link("teste", "https://google.com.br", "high", "text", "12345");
	}
	
	@Test
	public void testEquals() {
		Link test = new Link("teste", "https://google.com.br", "high", "text", "12345");
		Assert.assertTrue("Os links deveriam ser iguais.", this.link.equals(test));
	}
	
	@Test
	public void testNotEquals() {
		Link test = new Link("teste", "https://google.com.br", "high", "text", "56789");
		Assert.assertFalse("Os links deveriam ser diferentes.", this.link.equals(test));
	}
}
