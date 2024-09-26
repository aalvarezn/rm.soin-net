package com.soin.sgrm.model.pos.wf;

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
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TRAMITES_ENLACE_RFC")
public class PEdgeRFC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tramites_enlace_rfc_seq")
	@SequenceGenerator(name = "tramites_enlace_rfc_seq", sequenceName = "tramites_enlace_rfc_id_seq", allocationSize = 1)
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"FROM_NODE_ID\"", nullable = true)
	private PNodeRFC nodeFrom;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"TO_NODE_ID\"", nullable = true)
	private PNodeRFC nodeTo;

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

	public PNodeRFC getNodeFrom() {
		return nodeFrom;
	}

	public void setNodeFrom(PNodeRFC nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public PNodeRFC getNodeTo() {
		return nodeTo;
	}

	public void setNodeTo(PNodeRFC nodeTo) {
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