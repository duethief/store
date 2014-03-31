package com;

import org.junit.*;

public class AppTest {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("#1. BeforeClass");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("#7. AfterClass");
	}
	
	@Before
	public void before() {
		System.out.println("#2. Before");
	}
	
	@After
	public void after() {
		System.out.println("#3. After");
	}
	
	@Test
	public void test01() {
		System.out.println("#4. Test01");
	}
	
	@Test
	public void test02() {
		System.out.println("#5. Test02");
	}
	
	@Test
	public void test03() {
		System.out.println("#6. Test03");
	}
}
