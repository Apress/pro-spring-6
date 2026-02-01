package com.apress.prospring6.two;

import com.apress.prospring6.two.decoupled.MessageRenderer;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by iuliana.cosmina on 1/28/17.
 */
public class TestHelloWorldSpringDI {

	@Test
	public void testHello() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/app-context.xml");
		MessageRenderer mr = ctx.getBean("renderer", MessageRenderer.class);
		assertNotNull(mr);
	}
}
