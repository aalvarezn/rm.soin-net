package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.pos.PModifiedComponent;

public interface PModifiedComponentService {

	List<PModifiedComponent> list() throws SQLException;
	
	PModifiedComponent findById(Integer id);
}
