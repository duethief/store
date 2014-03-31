package com.bookstore.domain;

public enum UserLevel {
	NORMAL(0), READER(1), MVP(2);
	
	private int value;
	
	private UserLevel(int value) {
		this.value = value;
	}
	
	public int intValue() {
		return this.value;
	}

	public static UserLevel valueOf(int value) {
		switch(value) {
		case 0: return NORMAL;
		case 1: return READER;
		case 2: return MVP;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}
