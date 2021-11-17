package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Authority;

public interface AuthorityService {

	List<Authority> list();

	Authority findById(Integer id);

}
