package com.soin.sgrm.dao.pos;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemProject;
import com.soin.sgrm.utils.AliasToBeanNestedResultTransformer;

@Repository("systemDao")
public class SystemDaoImpl extends AbstractDao<Long, PSystem> implements SystemDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<PSystem> listWithProject() {
		List<PSystem> results = new ArrayList<PSystem>();
		try {
//			String sql = "SELECT s.\"CODIGO\" as code , s.\"NOMBRE\" as name FROM public.\"SISTEMA\" s ";
//			SQLQuery query = getSession().createSQLQuery(sql);
//			query.setResultTransformer(Transformers.aliasToBean(PSystem.class));
//			results = query.list();

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("code").as("code"));
			projectionList.add(Projections.property("name").as("name"));
			projectionList.add(Property.forName("pc.code").as("projectCodes.code"));

			Criteria criteriaSystem = getSession().createCriteria(PSystem.class);
			criteriaSystem.createAlias("projectCodes", "pc");
			criteriaSystem.setProjection(projectionList);

			criteriaSystem.setResultTransformer(new AliasToBeanNestedResultTransformer(PSystem.class));
			results = criteriaSystem.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

}
