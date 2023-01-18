package com.soin.sgrm.model.wf;

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

import com.soin.sgrm.utils.Constant;

@SuppressWarnings("serial")
@Entity
@Table(name = "TRAMITES_TRAMITE")
public class WorkFlowRFC implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAMITES_TRAMITE_SQ")
	@SequenceGenerator(name = "TRAMITES_TRAMITE_SQ", sequenceName = "TRAMITES_TRAMITE_SQ", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	@NotEmpty(message = Constant.EMPTY)
	@Size(max = 50, message = "Máximo 50 caracteres.")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SISTEMA_ID", nullable = true)
	private WFSystem system;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "workFlow", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NodeRFC> nodes = new ArrayList<NodeRFC>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TIPO", nullable = true)
	private Type type;
	
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

	public WFSystem getSystem() {
		return system;
	}

	public void setSystem(WFSystem system) {
		this.system = system;
	}

	public List<NodeRFC> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeRFC> nodes) {
		this.nodes = nodes;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
}