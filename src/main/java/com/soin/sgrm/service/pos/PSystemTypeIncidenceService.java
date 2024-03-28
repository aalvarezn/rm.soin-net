
package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystemTypeIncidence;



public interface PSystemTypeIncidenceService extends BaseService<Long,PSystemTypeIncidence> {

	List<PSystemTypeIncidence> listTypePetition();

	List<PSystemTypeIncidence> findBySystem(Integer id);

	PSystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidence);

	List<PSystemTypeIncidence> findByManager(Integer idUser);

}