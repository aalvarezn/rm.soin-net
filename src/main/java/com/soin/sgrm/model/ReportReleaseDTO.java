package com.soin.sgrm.model;

import java.io.ByteArrayInputStream;

public class ReportReleaseDTO {
	
	private String fileName;
	private ByteArrayInputStream stream;
	private int length;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ByteArrayInputStream getStream() {
		return stream;
	}
	public void setStream(ByteArrayInputStream stream) {
		this.stream = stream;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	
	
}
