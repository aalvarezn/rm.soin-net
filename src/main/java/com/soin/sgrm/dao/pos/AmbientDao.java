package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAmbient;

@Repository("actionDao")
public interface AmbientDao extends BaseDao<Long, PAmbient> {

}
