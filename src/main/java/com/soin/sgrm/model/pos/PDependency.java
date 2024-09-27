package com.soin.sgrm.model.pos;

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
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "RELEASES_RELEASE_DEPENDENCIAS")
public class PDependency implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "releases_dependencias_seq")
	@SequenceGenerator(name = "releases_dependencias_seq", sequenceName = "releases_dependencias_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Cascade({ CascadeType.MERGE, CascadeType.DETACH, CascadeType.EVICT })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"FROM_RELEASE_ID\"", nullable = false)
	private PReleaseUser release;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TO_RELEASE_ID\"", nullable = false)
	private PReleaseUser to_release;

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

	public PReleaseUser getRelease() {
		return release;
	}

	public void setRelease(PReleaseUser release) {
		this.release = release;
	}

	public PReleaseUser getTo_release() {
		return to_release;
	}

	public void setTo_release(PReleaseUser to_release) {
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

	public boolean equals(PDependency obj) {
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
