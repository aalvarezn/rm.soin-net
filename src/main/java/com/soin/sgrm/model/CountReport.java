package com.soin.sgrm.model;

import javax.persistence.Transient;

public class CountReport {
	@Transient
	private
	String label;
	@Transient
	private int value1;
	@Transient
	private int value2;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getValue1() {
		return value1;
	}
	public void setValue1(int value1) {
		this.value1 = value1;
	}
	public int getValue2() {
		return value2;
	}
	public void setValue2(int value2) {
		this.value2 = value2;
	}

	
	
}