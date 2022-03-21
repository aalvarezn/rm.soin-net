package com.soin.sgrm.utils;

import java.util.ArrayList;

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.utils.MyError;

public class JsonResponse {
	private String status = null;
	private String message = null;
	private String exception = null;
	private ArrayList<MyError> errors = new ArrayList<MyError>();
	private String data;
	private Object obj;
	private ButtonCommand button;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<MyError> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<MyError> errors) {
		this.errors = errors;
	}

	public void addError(String field, String message) {
		errors.add(new MyError(field, message));
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public ButtonCommand getButton() {
		return button;
	}

	public void setButton(ButtonCommand button) {
		this.button = button;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}