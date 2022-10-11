package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RELEASES_RELEASE_OBJETOS")
public class Release_Objects implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private int id;

	@Id
	@Column(name = "RELEASE_ID")
	private int releaseId;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "OBJETO_ID", nullable = true)
	private ReleaseObject objects = new ReleaseObject();

	public ReleaseObject getObjects() {
		return objects;
	}

	public void setObjects(ReleaseObject objects) {
		this.objects = objects;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(int releaseId) {
		this.releaseId = releaseId;
	}




}