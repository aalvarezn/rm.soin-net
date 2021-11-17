package com.soin.sgrm.utils;

public class ItemObject {

	private String id;
	private String name;
	private String revisionSVN;
	private String revisionDate;
	private String descriptionItem;
	private String objectType;
	private String objectConfigurationItem;
	private String execute;
	private String dbScheme;
	private String executePlan;
	private String isSql;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRevisionSVN() {
		return revisionSVN;
	}

	public void setRevisionSVN(String revisionSVN) {
		this.revisionSVN = revisionSVN;
	}

	public String getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(String revisionDate) {
		this.revisionDate = revisionDate;
	}

	public String getDescriptionItem() {
		return descriptionItem;
	}

	public void setDescriptionItem(String descriptionItem) {
		this.descriptionItem = descriptionItem;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectConfigurationItem() {
		return objectConfigurationItem;
	}

	public void setObjectConfigurationItem(String objectConfigurationItem) {
		this.objectConfigurationItem = objectConfigurationItem;
	}

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	public String getDbScheme() {
		return dbScheme;
	}

	public void setDbScheme(String dbScheme) {
		this.dbScheme = dbScheme;
	}

	public String getExecutePlan() {
		return executePlan;
	}

	public void setExecutePlan(String executePlan) {
		this.executePlan = executePlan;
	}

	public String getIsSql() {
		return isSql;
	}

	public void setIsSql(String isSql) {
		this.isSql = isSql;
	}

}
