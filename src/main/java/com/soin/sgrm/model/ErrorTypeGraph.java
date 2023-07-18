package com.soin.sgrm.model;

import javax.persistence.Transient;

public class ErrorTypeGraph {
	@Transient
	private
	String label;
	String label2;
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
	public String getLabel2() {
		return label2;
	}
	public void setLabel2(String label2) {
		this.label2 = label2;
	}
	
	
}
