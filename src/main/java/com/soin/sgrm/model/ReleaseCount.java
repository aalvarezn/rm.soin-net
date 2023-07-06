package com.soin.sgrm.model;

import javax.persistence.Transient;

public class ReleaseCount {
	@Transient
	private String nameRelease;
	
	@Transient
	private String status;
	
	@Transient
	private Integer count;

	public String getNameRelease() {
		return nameRelease;
	}

	public void setNameRelease(String nameRelease) {
		this.nameRelease = nameRelease;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
