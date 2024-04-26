package com.soin.sgrm.service.pos;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestBaseR1FastDao;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PRequestBaseR1Fast;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.Constant;

@Service("PRequestBaseR1FastService")
@Transactional("transactionManagerPos")
public class PRequestBaseR1FastServiceImpl implements PRequestBaseR1FastService {

	@Autowired
	PRequestBaseR1FastDao dao;
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(PRequestBaseR1FastServiceImpl.class);
	@Override
	public PRequestBaseR1Fast findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestBaseR1Fast findByKey(String name, String value) {

		return dao.getByKey(name,value);
	}

	@Override
	public List<PRequestBaseR1Fast> findAll() {
	
		return dao.findAll();
	}

	@Override
	public void save(PRequestBaseR1Fast model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestBaseR1Fast model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestBaseR1Fast model) {
		dao.update(model);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public JsonSheet<PRequestBaseR1Fast> findAllRequest(Integer id, Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Integer systemId,
			Long typePetitionId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
				.createAlias("managers", "managers").add(Restrictions.eq("managers.id", id)).list();
		alias.put("status", "status");
		alias.put("user", "user");
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
			columns.put("status", Restrictions.not(Restrictions.in("status.name", Constant.FILTREDRFC)));
		}

		if (systemId != 0) {
			alias.put("systemInfo", "systemInfo");
			columns.put("siges", Restrictions.or(Restrictions.eq("systemInfo.id", systemId)));

		} else {
			List<Integer> listaId = new ArrayList<Integer>();
			for (PSystemInfo system : systems) {
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



	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<PRequestBaseR1Fast> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Integer systemId, Long typePetitionId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("status", "status");
		alias.put("user", "user");

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


}
