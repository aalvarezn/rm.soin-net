package com.soin.sgrm.model;

import javax.persistence.Transient;

public class ErrorTypeGraph {
	@Transient
	private
	String label;
	@Transient
	private int value;
	private int valueRequest;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValueRequest() {
		return valueRequest;
	}
	public void setValueRequest(int valueRequest) {
		this.valueRequest = valueRequest;
	}
	
	
}
