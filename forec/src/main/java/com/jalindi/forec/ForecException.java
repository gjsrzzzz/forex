package com.jalindi.forec;

public class ForecException extends Exception {
	private static final long serialVersionUID = 1L;

	public ForecException(Exception ex) {
		super(ex);
	}

	public ForecException(String message, Exception e) {
		super(message, e);
	}

}
