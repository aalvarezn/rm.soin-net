package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAction;
@Repository("actionDao")
public class ActionDaoImpl extends AbstractDao<Long, PAction> implements ActionDao {

}
