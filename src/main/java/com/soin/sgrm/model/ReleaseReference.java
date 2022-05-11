package com.soin.sgrm.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class ReleaseReference implements Serializable, Cloneable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

}
