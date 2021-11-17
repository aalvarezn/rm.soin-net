package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.ReleaseFile;

public interface ReleaseFileService {

	void save(Integer id, ReleaseFile releaseFile) throws Exception;

	ReleaseFile findReleaseFile(String path);

	ReleaseFile findReleaseFileById(Integer id);

	void deleteReleaseFile(ReleaseFile releaseFile) throws Exception;
	
	List<String> ImpactObjects(Integer release_id);

}
