package com.soin.sgrm.utils;

public class MyError {
	private String key = null;
	private String message = null;

	public MyError(String key, String message) {
		super();
		this.key = key;
		this.message = message;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
