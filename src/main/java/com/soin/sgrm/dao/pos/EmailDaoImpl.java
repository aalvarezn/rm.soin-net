package com.soin.sgrm.dao.pos;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PEmail;

@Repository("emailDao")
public class EmailDaoImpl extends AbstractDao<Long, PEmail> implements EmailDao {

}
