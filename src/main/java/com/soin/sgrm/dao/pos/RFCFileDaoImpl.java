package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRFCFile;

@Repository("rfcFileDao")
public class RFCFileDaoImpl  extends AbstractDao<Long, PRFCFile> implements RFCFileDao   {

}
