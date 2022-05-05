package com.soin.sgrm.dao.pos;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.pos.PRFC;

@Repository("rfcDao")
public class RFCDaoImpl extends AbstractDao<Long, PRFC> implements RFCDao {
	
	
	
	@Override
	public Integer existNumRelease(String numRequest) {

		Criteria crit = getSession().createCriteria(PRFC.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();

		return recordsTotal;

	}
	@Override
	public void updateStatusRFC(PRFC rfc,String dateChange) throws Exception {
	
		String sql = "";
		Query query = null;
		try {
			

			String dateChangeUpdate = (dateChange != null && !dateChange.equals("")
					? "to_date('" + dateChange + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update public.\"RFC\" set \"ID_ESTADO\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHASOLICITUD\" = "
							+ dateChangeUpdate + "  where \"ID\" = %s",
					rfc.getStatus().getId(), rfc.getOperator(), rfc.getMotive(),
					rfc.getId());

			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			
			throw e;
		} 

	}
}
