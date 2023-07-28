package com.soin.sgrm.utils;

import java.util.ArrayList;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.ReleaseObjectEdit;

public class BulkLoad {

	private ArrayList<?> objects;
	private ArrayList<?> dependencies;

	public ArrayList<?> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<?> objects) {
		this.objects = objects;
	}

	public ArrayList<?> getDependencies() {
		return dependencies;
	}

	public void setDependencies(ArrayList<?> dependencies) {
		this.dependencies = dependencies;
	}

}
