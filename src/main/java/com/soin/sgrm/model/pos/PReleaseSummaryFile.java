package com.soin.sgrm.model.pos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.soin.sgrm.model.SystemInfo;

@SuppressWarnings("serial")
@Entity
@Table(name = "RELEASES_RELEASE")
public class PReleaseSummaryFile implements Serializable {

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "NUMERO_RELEASE")
	private String releaseNumber;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private SystemInfo system;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "RELEASES_RELEASE_REQUERIMIENTO", joinColumns = {
			@JoinColumn(name = "\"RELEASE_ID\"") }, inverseJoinColumns = { @JoinColumn(name = "\"REQUERIMIENTO_ID\"") })
	private Set<PRequest> requestList = new HashSet<PRequest>();

	

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


	public SystemInfo getSystem() {
		return system;
	}

	public void setSystem(SystemInfo system) {
		this.system = system;
	}

	public Set<PRequest> getRequestList() {
		return requestList;
	}

	public void setRequestList(Set<PRequest> requestList) {
		this.requestList = requestList;
	}

	public String getManager() {
		String name = "";
		int i = 0;
		for (PRequest request : this.requestList) {
			if (i == 0) {
				name = request.getSoinManagement();
			}
			i++;
		}
		return name;
	}

	public String getIdentificator() {
		if (this.requestList.size() > 0) {
			for (PRequest request : this.requestList) {
				return request.getCode_soin() + " " + (request.getCode_ice() != null ? request.getCode_ice() : "") + " "
						+ request.getDescription();
			}
		}
		return "";
	}

	public String getTPO_BT() {
		if (this.requestList.size() > 0) {
			for (PRequest request : this.requestList) {
				return request.getCode_soin();
			}
		}
		return "";
	}


}