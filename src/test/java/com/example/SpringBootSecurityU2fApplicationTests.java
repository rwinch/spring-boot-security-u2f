package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import sample.U2fApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = U2fApplication.class)
@WebAppConfiguration
public class SpringBootSecurityU2fApplicationTests {

	@Test
	public void contextLoads() {
	}

}
