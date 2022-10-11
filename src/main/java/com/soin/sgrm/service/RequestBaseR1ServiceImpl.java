package com.soin.sgrm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RequestBaseR1Dao;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.Constant;

@Service("RequestBaseR1Service")
@Transactional("transactionManager")
public class RequestBaseR1ServiceImpl implements RequestBaseR1Service {

	@Autowired
	RequestBaseR1Dao dao;
	@Autowired
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(RequestBaseR1ServiceImpl.class);
	@Override
	public RequestBaseR1 findById(Long id) {
		return dao.getById(id);
	}
	@Override
	public RequestBaseR1 findByR1(Long id) {
		return dao.getByIdR1(id);
	}
	@Override
	public RequestBaseR1 findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RequestBaseR1> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(RequestBaseR1 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		RequestBaseR1 model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(RequestBaseR1 model) {
		dao.update(model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<RequestBaseR1> findAllRequest(Integer id, Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Integer systemId,
			Long typePetitionId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
				.createAlias("managers", "managers").add(Restrictions.eq("managers.id", id)).list();
		alias.put("status", "status");

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

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (statusId == 0) {
			statusId = null;
		}
		if (typePetitionId == 0) {
			typePetitionId = null;
		}

		if (typePetitionId != null) {

			columns.put("typePetition", Restrictions.eq("typePetition.id", typePetitionId));
		} 

		if (statusId != null) {

			columns.put("status", Restrictions.eq("status.id", statusId));
		} else {
			columns.put("status", Restrictions.in("status.name", Constant.FILTREDRFC));
		}

		if (systemId != 0) {
			alias.put("systemInfo", "systemInfo");
			columns.put("siges", Restrictions.or(Restrictions.eq("systemInfo.id", systemId)));

		} else {
			List<Integer> listaId = new ArrayList<Integer>();
			for (SystemInfo system : systems) {
				listaId.add(system.getId());
			}
			alias.put("systemInfo", "systemInfo");
			columns.put("systemInfo", (Restrictions.in("systemInfo.id", listaId)));
		}

		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		*/
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,1);

	}



	@Override
	public JsonSheet<RequestBaseR1> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Integer systemId, Long typePetitionId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("status", "status");

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

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (statusId == 0) {
			statusId = null;
		}
		if (typePetitionId == 0) {
			typePetitionId = null;
		}

		if (typePetitionId != null) {

			columns.put("typePetition", Restrictions.eq("typePetition.id", typePetitionId));
		} 

		if (statusId != null) {

			columns.put("status", Restrictions.eq("status.id", statusId));
		}

		if (systemId != 0) {
			alias.put("systemInfo", "systemInfo");
			columns.put("siges", Restrictions.or(Restrictions.eq("systemInfo.id", systemId)));

		} 

		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		*/
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,1);
	}

	@Override
	public Integer countByType(Integer id, String type, int query, Object[] object) {
		
		return dao.countByType(id, type, query, object);
	}

}
