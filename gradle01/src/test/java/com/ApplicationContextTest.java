package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:sqlExecutorContext.xml")
public class ApplicationContextTest {
	@Autowired
	ApplicationContext applicationContext;
	
	@Test
	public void getBeansInApplicationContext() {
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			System.out.println(beanName + " : " + applicationContext.getBean(beanName));
		}
	}

}
