package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.pos.PModifiedComponent;

public interface PModifiedComponentDao {
	
	List<PModifiedComponent> list() throws SQLException;
	
	PModifiedComponent findById(Integer id);

}
