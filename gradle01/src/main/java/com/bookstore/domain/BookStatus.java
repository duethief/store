package com.bookstore.domain;

public enum BookStatus {
	CanRent(0), RentNow(1), Missing(2);
	
	private int value;
	
	private BookStatus(int value) {
		this.value = value;
	}
	
	public int intValue() {
		return this.value;
	}

	public static BookStatus valueOf(int value) {
		switch(value) {
		case 0: return CanRent;
		case 1: return RentNow;
		case 2: return Missing;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}
