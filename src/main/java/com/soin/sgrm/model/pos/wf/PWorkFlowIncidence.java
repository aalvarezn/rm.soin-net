package com.soin.sgrm.model.pos.wf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.soin.sgrm.model.Status;
import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAMITES_TRAMITE")
public class PWorkFlowIncidence implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tramites_tramite_seq")
	@SequenceGenerator(name = "tramites_tramite_seq", sequenceName = "tramites_tramite_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"SISTEMA_ID\"", nullable = true)
	private PWFSystem system;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "workFlow", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PNodeIncidence> nodes = new ArrayList<PNodeIncidence>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"ID_TIPO\"", nullable = true)
	private PType type;
	
	@Transient
	private Integer systemId;

	@Transient
	private Integer typeId;
	

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

	public PWFSystem getSystem() {
		return system;
	}

	public void setSystem(PWFSystem system) {
		this.system = system;
	}

	public List<PNodeIncidence> getNodes() {
		return nodes;
	}

	public void setNodes(List<PNodeIncidence> nodes) {
		this.nodes = nodes;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public PType getType() {
		return type;
	}

	public void setType(PType type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
}
