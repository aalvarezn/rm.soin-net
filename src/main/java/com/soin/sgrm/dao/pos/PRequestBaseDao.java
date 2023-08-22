package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingToError;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.model.pos.PRequestBase;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestReport;

public interface PRequestBaseDao extends BaseDao<Long, PRequestBase> {

	Integer existNumRequest(String numRequest);

	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	PRequestBaseR1 getByIdR1(Long id);

	List<PRequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	PRequestReport findByReport(Long id); 

}
