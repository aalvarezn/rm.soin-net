package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.EmailIncidence;

public interface EmailIncidenceService extends BaseService<Long,EmailIncidence> {

	List<EmailIncidence> listTypePetition();

	List<EmailIncidence> findBySystem(Integer id);

	EmailIncidence findByIdAndSys(Integer systemId, Long typeIncidence);

}
