package com.soin.sgrm.service.pos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.UserDao;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;

@Service("userService")
@Transactional("transactionManagerPos")
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	UserDao dao;

	@Override
	public PUser findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PUser> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PUser model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PUser model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PUser model) {
		dao.update(model);
	}

	@Override
	public PUser findById(Long id) {
		return dao.getById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<PUser> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Integer sStatus, String dateRange, PUser userLogin) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("requestDate", Restrictions.between("requestDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if (sStatus != null)
			columns.put("active", Restrictions.eq("active", (sStatus == 1 ? true : false)));

		if (userLogin != null)
			columns.put("userName", Restrictions.ne("userName", userLogin.getUserName()));

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(Restrictions.like("userName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("email", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}

		List<String> fetchs = new ArrayList<String>();

		JsonSheet<PUser> list = dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias);
		return list;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		PUser user = findByKey("userName", username);
		if (user == null) {
			throw new BadCredentialsException("Usuario o contraseña incorrecto");
		}
		return user;
	}

	@Override
	public List<PUser> findAllColumns(String[] columns) {
		return dao.findAllColumns(columns);
	}

	@Override
	public List<PUser> findbyUserName(String[] userNames) {
		return dao.findbyUserName(userNames);
	}

}
