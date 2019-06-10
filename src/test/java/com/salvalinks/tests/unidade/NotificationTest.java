package com.salvalinks.tests.unidade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.salvalinks.models.Notification;


@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationTest {
	
	private Notification notification;
	private String time;
	private Date date;
	
	@Before
	public void setUp() throws ParseException {
		this.time = "10/06/2019 11:15";
		this.date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time);
		this.notification = new Notification("teste", "teste", date);
	}
	
	@Test
	public void testEquals() {
		Notification notification = new Notification("teste", "teste", date);
		Assert.assertTrue("Os grupos deveriam ser iguais.", notification.equals(this.notification));
	}
	
	@Test
	public void testNotEquals() {
		Notification notification = new Notification("teste1213", "teste", date);
		Assert.assertFalse("Os grupos deveriam ser diferentes.", notification.equals(this.notification));
	}
}
