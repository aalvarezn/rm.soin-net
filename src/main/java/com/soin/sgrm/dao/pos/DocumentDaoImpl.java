package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PDocument;

@Repository("documentDao")
public class DocumentDaoImpl extends AbstractDao<Long, PDocument> implements DocumentDao{

}
