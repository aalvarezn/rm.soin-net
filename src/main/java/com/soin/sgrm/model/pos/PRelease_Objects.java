package com.soin.sgrm.model.pos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RELEASES_RELEASE_OBJETOS")
public class PRelease_Objects implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@Id
	@Column(name = "\"RELEASE_ID\"")
	private int releaseId;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"OBJETO_ID\"", nullable = true)
	private PReleaseObject objects = new PReleaseObject();

	public PReleaseObject getObjects() {
		return objects;
	}

	public void setObjects(PReleaseObject objects) {
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