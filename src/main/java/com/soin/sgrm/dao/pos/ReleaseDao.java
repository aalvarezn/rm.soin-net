package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface ReleaseDao extends BaseDao<Long, PRelease> {
	 List<PRelease> listReleasesBySystem1(Long id);
	 JsonSheet<?> listReleasesBySystem(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Long systemId) throws SQLException, ParseException;
}
