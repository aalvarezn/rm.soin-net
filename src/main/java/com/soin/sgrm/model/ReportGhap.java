package com.soin.sgrm.model;

import javax.persistence.Transient;

public class ReportGhap {
	@Transient
	private
	String label;
	@Transient
	private String labelStatus;
	private int value;
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
	public String getLabelStatus() {
		return labelStatus;
	}
	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}

	
	
}
