package com.soin.sgrm.model;

public class Tree {

	private String father;

	private Integer fatherId;

	private String children;

	private Integer childrenId;

	private Integer depthNode;

	private String status;

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public Integer getChildrenId() {
		return childrenId;
	}

	public void setChildrenId(Integer childrenId) {
		this.childrenId = childrenId;
	}

	public Integer getDepthNode() {
		return depthNode;
	}

	public void setDepthNode(Integer depthNode) {
		this.depthNode = depthNode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
