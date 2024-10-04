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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SigesDao;
import com.soin.sgrm.dao.pos.PButtonInfraDao;
import com.soin.sgrm.dao.pos.PSigesDao;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.pos.PButtonInfra;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.Constant;

@Service("PButtonInfraService")
@Transactional("transactionManagerPos")
public class PButtonInfraServiceImpl implements PButtonInfraService{

	@Autowired
	PButtonInfraDao dao;
	
	@Override
	public PButtonInfra findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PButtonInfra findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PButtonInfra> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PButtonInfra model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PButtonInfra model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PButtonInfra model) {
		dao.update(model);
	}

	@Override
	public List<PButtonInfra> listCodeSiges(Integer id) {
		return dao.listCodeSiges(id);
	}

	@Override
	public boolean checkUniqueCode(String codeSiges) {
		return dao.checkUniqueCode(codeSiges);
	}
	@Override
	public boolean veryUpdateSigesCode(Long id, String codeSiges) {
		
		return dao.veryUpdateSigesCode(id,codeSiges);
	}

	@Override
	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges) {
		return dao.veryUpdateSigesCodeDif(id,codeSiges);
	}

	@Override
	public boolean existsBySystemId(Integer systemId) {
		
		return dao.existsBySystemId(systemId);
	}

	@Override
	public boolean existsBySystemIdandId(Long sId, Integer systemId) {
		return dao.existsBySystemIdandId(sId,systemId);
	}

	@Override
	public JsonSheet<?> findAllButton(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId,List<PSystem> systems) {
		
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("system", "system");

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(

					Restrictions.like("name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (systemId != 0) {
			
			columns.put("system", Restrictions.or(Restrictions.eq("system.id", systemId)));

		} else {
			List<Integer> listaId = new ArrayList<Integer>();
			for (PSystem system : systems) {
				listaId.add(system.getId());
			}
			
			columns.put("system", (Restrictions.in("system.id", listaId)));
		}

		List<String> fetchs = new ArrayList<String>();
		/*fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		*/
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias,4);
	}


}
