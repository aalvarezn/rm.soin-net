package com.soin.sgrm.model.wf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TRAMITES_ENLACE_RFC")
public class EdgeRFC implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FROM_NODE_ID", nullable = true)
	private NodeRFC nodeFrom;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TO_NODE_ID", nullable = true)
	private NodeRFC nodeTo;

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

	public NodeRFC getNodeFrom() {
		return nodeFrom;
	}

	public void setNodeFrom(NodeRFC nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public NodeRFC getNodeTo() {
		return nodeTo;
	}

	public void setNodeTo(NodeRFC nodeTo) {
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