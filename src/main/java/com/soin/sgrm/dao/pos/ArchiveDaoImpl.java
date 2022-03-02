package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PArchive;

@Repository("archiveDao")
public class ArchiveDaoImpl extends AbstractDao<Long, PArchive> implements ArchiveDao {

}
