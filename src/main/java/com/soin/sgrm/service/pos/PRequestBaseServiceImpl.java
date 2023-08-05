package com.soin.sgrm.service.pos;

import java.sql.SQLException;
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

import com.soin.sgrm.dao.pos.PRequestBaseDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBase;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("PRequestBaseService")
@Transactional("transactionManagerPos")
public class PRequestBaseServiceImpl implements PRequestBaseService {

	@Autowired
	PRequestBaseDao dao;
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(PRequestBaseServiceImpl.class);
	@Override
	public PRequestBase findById(Long id) {
		return dao.getById(id);
	}
	@Override
	public PRequestBaseR1 findByR1(Long id) {
		return dao.getByIdR1(id);
	}
	@Override
	public PRequestBase findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PRequestBase> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PRequestBase model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestBase model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestBase model) {
		dao.update(model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<PRequestBase> findAllRequest(Integer id, Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Integer systemId,
			Long typePetitionId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
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
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

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

	@Override
	public String generateRequestNumber(String codeProject,String description,String codeSystem) {
		String numRequest = "";
		String partCode = "";
		try {

			partCode =  codeProject ;

			numRequest = verifySecuence(partCode,description.toUpperCase(),codeSystem.toUpperCase());

		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			numRequest = "Sin Asignar";
		}
		return numRequest;
	}
	
	public String verifySecuence(String partCode,String description,String codeSystem) {
		String numRequest = "";
		try {
			
			partCode =  description +"_"+partCode+"_"+codeSystem+ "_SC";
			int amount = existNumRequest(partCode);

			if (amount == 0) {
				numRequest = partCode + "_01_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRequest;
			} else {
				if (amount < 10) {
					numRequest = partCode+"_0" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
					return numRequest;
				}
				numRequest = partCode+"_" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRequest;
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";
	}

	public Integer existNumRequest(String number_release) throws SQLException {
		return dao.existNumRequest(number_release);
	}

	@Override
	public JsonSheet<PRequestBase> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
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
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

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
	@Override
	public List<PRequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		
		return dao.listByAllSystemError( dateRange,  systemId);
	}
	@Override
	public PRequestReport findByReport(Long id) {
		// TODO Auto-generated method stub
		return dao.findByReport(id);
	}
}
