package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PGDocConfiguration;

@Repository("gDocConfigurationDao")
public class GDocConfigurationDaoImpl extends AbstractDao<Long, PGDocConfiguration> implements GDocConfigurationDao {

}
