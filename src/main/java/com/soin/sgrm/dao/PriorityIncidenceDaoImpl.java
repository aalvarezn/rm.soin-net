package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.Risk;

@Repository
public class PriorityIncidenceDaoImpl extends AbstractDao<Long, PriorityIncidence> implements PriorityIncidenceDao {
}
