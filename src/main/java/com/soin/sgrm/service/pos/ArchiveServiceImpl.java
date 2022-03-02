package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.pos.PArchive;

@Service("archiveService")
@Transactional("transactionManagerPos")
public class ArchiveServiceImpl implements ArchiveService{

	@Override
	public PArchive findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PArchive findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PArchive> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PArchive model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PArchive model) {
		// TODO Auto-generated method stub
		
	}

}
