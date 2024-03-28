package com.soin.sgrm.utils;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class ReleaseByTime implements Serializable {

	private String year;
	private String status;
	private String amount;

	public ReleaseByTime() {
		super();
	}

//	public ReleaseByTime(String year, String status, String amount) {
//		super();
//		this.year = year;
//		this.status = status;
//		this.amount = amount;
//	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
