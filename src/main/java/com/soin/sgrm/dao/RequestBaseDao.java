package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingToError;
import com.soin.sgrm.model.RequestReport;

public interface RequestBaseDao extends BaseDao<Long, RequestBase> {

	Integer existNumRequest(String numRequest);

	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 getByIdR1(Long id);

	List<RequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	RequestReport findByReport(Long id); 

}
