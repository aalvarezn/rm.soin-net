package com.soin.sgrm.utils;

import java.util.ArrayList;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.ReleaseObjectEdit;

public class BulkLoad {

	private ArrayList<ReleaseObjectEdit> objects;
	private ArrayList<Dependency> dependencies;

	public ArrayList<ReleaseObjectEdit> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<ReleaseObjectEdit> objects) {
		this.objects = objects;
	}

	public ArrayList<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(ArrayList<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

}
