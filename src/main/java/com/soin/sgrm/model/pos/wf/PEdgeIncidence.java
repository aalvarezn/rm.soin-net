package com.soin.sgrm.model.pos.wf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soin.sgrm.utils.Constant;

@Entity
@Table(name = "TRAMITES_ENLACE_INC")
public class PEdgeIncidence implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tramites_enlace_inc_seq")
	@SequenceGenerator(name = "tramites_enlace_inc_seq", sequenceName = "tramites_enlace_inc_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"FROM_NODE_ID\"", nullable = true)
	private PNodeIncidence nodeFrom;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TO_NODE_ID\"", nullable = true)
	private PNodeIncidence nodeTo;

	@Transient
	private Integer nodeFromId;

	@Transient
	private Integer nodeToId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PNodeIncidence getNodeFrom() {
		return nodeFrom;
	}

	public void setNodeFrom(PNodeIncidence nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public PNodeIncidence getNodeTo() {
		return nodeTo;
	}

	public void setNodeTo(PNodeIncidence nodeTo) {
		this.nodeTo = nodeTo;
	}

	public Integer getNodeFromId() {
		return nodeFromId;
	}

	public void setNodeFromId(Integer nodeFromId) {
		this.nodeFromId = nodeFromId;
	}

	public Integer getNodeToId() {
		return nodeToId;
	}

	public void setNodeToId(Integer nodeToId) {
		this.nodeToId = nodeToId;
	}

}
