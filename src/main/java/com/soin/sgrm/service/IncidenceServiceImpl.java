package com.soin.sgrm.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.IncidenceDao;
import com.soin.sgrm.dao.RequestBaseDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.IncidenceResume;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("IncidenceService")
@Transactional("transactionManager")
public class IncidenceServiceImpl implements IncidenceService {

	@Autowired
	IncidenceDao dao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	SystemService systemService;
	@Autowired
	AttentionGroupService attentionGroupService;
	public static final Logger logger = Logger.getLogger(IncidenceServiceImpl.class);
	@Override
	public Incidence findById(Long id) {
		return dao.getById(id);
	}
	@Override
	public Incidence getIncidences(Long id) {
		return dao.getIncidences(id);
	}
	@Override
	public Incidence findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Incidence> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Incidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Incidence model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Incidence model) {
		dao.update(model);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JsonSheet<Incidence> findAllRequest( Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Long typeId,
			Long priorityId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		
		alias.put("status", "status");
		alias.put("status.status", "statusFinal");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					columns.put("updateDate", Restrictions.between("updateDate", start, end));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("numTicket", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("createFor", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (statusId == 0) {
			statusId = null;
		}
		if (typeId == 0) {
			typeId = null;
		}
		if (priorityId == 0) {
			priorityId = null;
		}


		if (typeId != null) {
			alias.put("typeIncidence", "typeIncidence");
			alias.put("typeIncidence.typeIncidence", "typeIncidenceFinal");
			columns.put("typeIncidenceFinal", Restrictions.eq("typeIncidenceFinal.id", typeId));
		} 

		if (statusId != null) {

			columns.put("statusFinal", Restrictions.eq("statusFinal.id", statusId));
		}

		if (priorityId != null) {
			alias.put("priority", "priority");
			alias.put("priority.priority", "priorityFinal");
			columns.put("priorityFinal", Restrictions.eq("priorityFinal.id", priorityId));
		} 

		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		
		*/
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		fetchs.add("attentionGroup");
		fetchs.add("node");

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,3);


	}

	@Override

	public String generatTicketNumber(String nameSystem,String typeCode) {
		String numTicket = "";
		String partCode = "";
		 partCode = typeCode.substring(0,3);

		try {

			


			numTicket = verifySecuence(nameSystem,partCode);


		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			numTicket = "Sin Asignar";
		}
		return numTicket;
	}
	

	public String verifySecuence(String nameSystem,String partCode) {
		String numTicket = "";
		String nameTicket=nameSystem +"_TKT_"+partCode;
		nameTicket=nameTicket.toUpperCase();
		try {
			int amount = existNumTicket(nameTicket);

			if (amount == 0) {
				numTicket = nameTicket+ "_01_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numTicket;
			} else {
				if (amount < 10) {
					numTicket = nameTicket+ "_0" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
					return numTicket;
				}
				numTicket = nameTicket + "_" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");

				return numTicket;
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";
	}

	public Integer existNumTicket(String nameSystem) throws SQLException {
		return dao.existNumTicket(nameSystem);
	}

	

	@Override
	public Integer countByType(Integer id, String type, int query, Object[] object,Integer userLogin,String email) {
		
		return dao.countByType(id, type, query, object,userLogin,email);
	}
	@Override
	public JsonSheet<Incidence> findAllRequest(String email, Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId,Integer userLogin,Integer systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		
		
		alias.put("status", "status");
		alias.put("status.status", "statusFinal");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					columns.put("updateDate", Restrictions.between("updateDate", start, end));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("numTicket", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("createFor", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (statusId == 0) {
			statusId = null;
		}
		if (typeId == 0) {
			typeId = null;
		}
		if (priorityId == 0) {
			priorityId = null;
		}

		columns.put("createFor", Restrictions.like("createFor",email,MatchMode.ANYWHERE));
		if (typeId != null) {
			alias.put("typeIncidence", "typeIncidence");
			alias.put("typeIncidence.typeIncidence", "typeIncidenceFinal");
			columns.put("typeIncidenceFinal", Restrictions.eq("typeIncidenceFinal.id", typeId));
		} 

		if (statusId != null) {

			columns.put("statusFinal", Restrictions.eq("statusFinal.id", statusId));
		}

		if (priorityId != null) {
			alias.put("priority", "priority");
			alias.put("priority.priority", "priorityFinal");
			columns.put("priorityFinal", Restrictions.eq("priorityFinal.id", priorityId));
		
		} 
		
		if (systemId != 0) {
			alias.put("system", "system");
			columns.put("system", Restrictions.or(Restrictions.eq("system.id", systemId)));

		} 
		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		
		*/
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		fetchs.add("attentionGroup");
		fetchs.add("node");

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,3);


	}
	@Override
	public Incidence getIncidenceByName(String numTicket) {
		return dao.getIncidenceByName(numTicket);
	}
	@Override
	public List<IncidenceResume> getListIncideSLA() {
		return dao.getListIncideSLA();
	}
	@Override
	public void updateIncidenceResume(IncidenceResume incidenceResume) {
		dao.updateIncidenceResume(incidenceResume);
		
	}
	@Override
	

	public JsonSheet<Incidence> findAllRequest2(String email, Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId,
			Integer systemId, Integer userLogin) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		
		
		alias.put("status", "status");
		alias.put("status.status", "statusFinal");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					columns.put("updateDate", Restrictions.between("updateDate", start, end));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("numTicket", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("createFor", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (statusId == 0) {
			statusId = null;
		}
		if (typeId == 0) {
			typeId = null;
		}
		if (priorityId == 0) {
			priorityId = null;
		}

		if (typeId != null) {
			alias.put("typeIncidence", "typeIncidence");
			alias.put("typeIncidence.typeIncidence", "typeIncidenceFinal");
			columns.put("typeIncidenceFinal", Restrictions.eq("typeIncidenceFinal.id", typeId));
		} 

		if (statusId != null) {

			columns.put("statusFinal", Restrictions.eq("statusFinal.id", statusId));
		}

		if (priorityId != null) {
			alias.put("priority", "priority");
			alias.put("priority.priority", "priorityFinal");
			columns.put("priorityFinal", Restrictions.eq("priorityFinal.id", priorityId));
		
		} 
		
		if (systemId != 0) {
			alias.put("system", "system");
			columns.put("system", Restrictions.or(Restrictions.eq("system.id", systemId)));

		}else {
			List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(userLogin);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(AttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<System>systemsList= systemService.findByGroupIncidence(listAttentionGroupId);
			List<Integer> systemsListId=new ArrayList<Integer>();
			for(System system: systemsList){
				systemsListId.add(system.getId());
			}
			alias.put("system", "system");
			
			columns.put("system",Restrictions.in("system.id", systemsListId));

			
		}
		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		
		*/
		fetchs.add("files");
		fetchs.add("typeIncidence");
		fetchs.add("priority");
		fetchs.add("tracking");
		fetchs.add("user");
		fetchs.add("assigned");
		fetchs.add("attentionGroup");
		fetchs.add("node");

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,3);

	}
	@Override
	public List<IncidenceResume> getListIncideRequest() {
		// TODO Auto-generated method stub
		return dao.getListIncideRequest();
	}
}
