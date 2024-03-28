package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PReleaseFile;

public interface PReleaseFileService {

	void save(Integer id, PReleaseFile releaseFile) throws Exception;

	PReleaseFile findReleaseFile(String path);

	PReleaseFile findReleaseFileById(Integer id);

	void deleteReleaseFile(PReleaseFile releaseFile) throws Exception;
	
	List<String> ImpactObjects(Integer release_id);

}
