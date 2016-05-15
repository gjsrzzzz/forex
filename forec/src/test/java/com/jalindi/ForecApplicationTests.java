package com.jalindi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jalindi.forec.ForecBuilder;
import com.jalindi.forec.ForecClass;
import com.jalindi.forec.ForecObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ForecApplicationTests.class)
@ComponentScan("com.jalindi.forec")
@Configuration
public class ForecApplicationTests {
	@Autowired
	ForecBuilder builder;

	@Bean
	public ForecBuilder forecBuilder() {
		return new ForecBuilder();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void compileTest() {
		try {
			ForecClass forecClass = builder.compile();
			forecClass.generateSchema();
			ForecObject object = builder.createObject(forecClass);
			object.marshal();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
