package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PTypeChange;

@Repository("typeChangeDao")
public class TypeChangeDaoImpl extends AbstractDao<Long, PTypeChange> implements TypeChangeDao {


}
