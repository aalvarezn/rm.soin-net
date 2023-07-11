package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.dao.BaseDao;
import com.soin.sgrm.model.pos.PEmailTemplate;;

public interface PEmailTemplateDao  extends BaseDao<Integer, PEmailTemplate>{

	List<PEmailTemplate> listAll();

	PEmailTemplate findById(Integer id);

	void updateEmail(PEmailTemplate email);

	void saveEmail(PEmailTemplate email);

	void deleteEmail(Integer id);

	boolean existEmailTemplate(String name);

	boolean existEmailTemplate(String name, Integer id);

}
