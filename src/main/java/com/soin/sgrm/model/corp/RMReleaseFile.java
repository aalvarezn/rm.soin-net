package com.soin.sgrm.model.corp;

public class RMReleaseFile {

	private String filename;

	private String release;

	private Integer revision;

	private String username;

	public RMReleaseFile() {
	}

	public RMReleaseFile(String fileName, String release, Integer revision, String userName) {
		this.filename = fileName;
		this.release = release;
		this.revision = revision;
		this.username = userName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "RMReleaseFile [filename=" + filename + ", release=" + release + ", revision=" + revision + ", username="
				+ username + "]";
	}

}
