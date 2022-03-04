package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.TypeObject;

public class PReleaseObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "REVISION_RESPOSITORIO")
	private String revision_SVN;

	@Column(name = "FECHA_REVISION")
	private Date revision_Date;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"RELEASE_ID\"", nullable = false)
	private int release;

	@Column(name = "OCUPA_EJECUTAR")
	private int execute;

	@Column(name = "ESQUEMA")
	private String dbScheme;


	@Column(name = "PLAN_EJECUCION")
	private int executePlan;

	@Column(name = "SQL")
	private int isSql;

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

	public String getRevision_SVN() {
		return revision_SVN;
	}

	public void setRevision_SVN(String revision_SVN) {
		this.revision_SVN = revision_SVN;
	}

	public Date getRevision_Date() {
		return revision_Date;
	}

	public void setRevision_Date(Date revision_Date) {
		this.revision_Date = revision_Date;
	}

	public int getRelease() {
		return release;
	}

	public void setRelease(int release) {
		this.release = release;
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

	public int getExecutePlan() {
		return executePlan;
	}

	public void setExecutePlan(int executePlan) {
		this.executePlan = executePlan;
	}

	public int getIsSql() {
		return isSql;
	}

	public void setIsSql(int isSql) {
		this.isSql = isSql;
	}

}
