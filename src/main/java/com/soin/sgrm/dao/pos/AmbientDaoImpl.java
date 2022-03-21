package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAmbient;

@Repository("ambientDao")
public class AmbientDaoImpl extends AbstractDao<Long, PAmbient> implements AmbientDao {

}
