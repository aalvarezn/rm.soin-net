package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Date;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "SISTEMAS_OBJETO")
public class PReleaseObjectClean implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistemas_objeto_seq")
	@SequenceGenerator(name = "sistemas_objeto_seq", sequenceName = "sistemas_objeto_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;

	@Value("${execute:0}")
	@Column(name = "OCUPA_EJECUTAR")
	private int execute;

	@Column(name = "ESQUEMA")
	private String dbScheme;

	@Value("${isSql:0}")
	@Column(name = "SQL")
	private int isSql;

	@Transient
	private String numRelease;
	
	public PReleaseObjectClean() {

	}

	public PReleaseObjectClean(String name, String description, String revision_SVN, Date revision_Date
			)
			throws CloneNotSupportedException {
		super();
		this.id = 0;
		this.name = name;
		this.description = description;

		this.execute = 0;
		this.dbScheme = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public int getExecute() {
		return execute;
	}

	public void setExecute(int execute) {
		this.execute = execute;
	}

	public String getDbScheme() {
		return dbScheme;
	}

	public void setDbScheme(String dbScheme) {
		this.dbScheme = dbScheme;
	}



	public int getIsSql() {
		return isSql;
	}

	public void setIsSql(int isSql) {
		this.isSql = isSql;
	}


	public boolean equals(PReleaseObjectClean obj) {
		boolean equals = true;
		if (!this.name.equals(obj.getName())) {
			equals = false;
		}

		if (!this.description.equals(obj.getDescription())) {
			equals = false;
		}

		

		return equals;
	}
	
	public String getNumRelease() {
		return numRelease;
	}

	public void setNumRelease(String numRelease) {
		this.numRelease = numRelease;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
