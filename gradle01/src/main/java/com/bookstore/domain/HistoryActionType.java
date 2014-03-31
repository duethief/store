package com.bookstore.domain;

public enum HistoryActionType {
	RENT(0), RETURN(1);
	
	private int value;
	
	private HistoryActionType(int value) {
		this.value = value;
	}
	
	public int intValue() {
		return this.value;
	}

	public static HistoryActionType valueOf(int value) {
		switch(value) {
		case 0: return RENT;
		case 1: return RETURN;
		default: throw new AssertionError("Unknown value: " + value);
		}
	}
}
