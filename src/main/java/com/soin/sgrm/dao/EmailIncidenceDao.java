package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.EmailIncidence;

public interface EmailIncidenceDao extends BaseDao<Long, EmailIncidence> {

	List<EmailIncidence> listTypePetition();

	List<EmailIncidence> findBySystem(Integer id);

	EmailIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId);

}
