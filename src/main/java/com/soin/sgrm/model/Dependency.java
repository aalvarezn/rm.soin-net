package com.soin.sgrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE_DEPENDENCIAS")
public class Dependency implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELEASES_RELEASE_DEPENDEAB5_SQ")
	@SequenceGenerator(name = "RELEASES_RELEASE_DEPENDEAB5_SQ", sequenceName = "RELEASES_RELEASE_DEPENDEAB5_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FROM_RELEASE_ID", nullable = false)
	private ReleaseUser release;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TO_RELEASE_ID", nullable = false)
	private Release to_release;

	@Column(name = "OBLIGATORIA")
	private Boolean mandatory;

	@Column(name = "ES_FUNCIONAL")
	private Boolean isFunctional;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ReleaseUser getRelease() {
		return release;
	}

	public void setRelease(ReleaseUser release) {
		this.release = release;
	}

	public Release getTo_release() {
		return to_release;
	}

	public void setTo_release(Release to_release) {
		this.to_release = to_release;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getIsFunctional() {
		return isFunctional;
	}

	public void setIsFunctional(Boolean isFunctional) {
		this.isFunctional = isFunctional;
	}

	public boolean equals(Dependency obj) {
		boolean equals = false;
		if (this.to_release.getId() == obj.getTo_release().getId()) {
			equals = true;
		}
		return equals;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "DEPENDENCY [id=" + id + ", release=" + release.getId() + ", to_release=" + to_release.getId() + ", mandatory="
				+ mandatory + ", isFunctional=" + isFunctional + "]";
	}
	
	

}
