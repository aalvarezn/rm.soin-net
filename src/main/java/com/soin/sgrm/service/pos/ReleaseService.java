package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface ReleaseService  extends BaseService<Long, PRelease>{
	 List<PRelease> listReleasesBySystem1(Long id);
		public JsonSheet<?> listReleasesBySystem( int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Long systemId) throws SQLException, ParseException ;
}
