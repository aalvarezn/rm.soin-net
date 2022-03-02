package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PAuthority;

public interface AuthorityService extends BaseService<Long, PAuthority> {

	List<PAuthority> findByCode(String[] roles);

}
