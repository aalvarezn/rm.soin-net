package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PProject;

@Repository("projectDao")
public class ProjectDaoImpl extends AbstractDao<Long, PProject> implements ProjectDao {

}
