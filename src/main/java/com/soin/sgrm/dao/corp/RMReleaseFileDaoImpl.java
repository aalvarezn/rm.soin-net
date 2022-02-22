package com.soin.sgrm.dao.corp;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.corp.RMReleaseFile;

@Repository
public class RMReleaseFileDaoImpl implements RMReleaseFileDao {

	@Autowired
	@Qualifier("sessionFactoryCorp")
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<RMReleaseFile> listByRelease(ReleaseEdit release) {
		Session sessionObj = null;
		String sql = "";
		try {
			sessionObj = sessionFactory.openSession();
			sql = String.format(
					" SELECT r.archivo as filename, r.release, r.revision, r.usuario as username FROM RM_OP.rm_release_file r WHERE r.release = %s ",
					"'" + release.getReleaseNumber() + "'");

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql).addScalar("filename", StandardBasicTypes.STRING)
					.addScalar("release", StandardBasicTypes.STRING).addScalar("revision", StandardBasicTypes.INTEGER)
					.addScalar("username", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(RMReleaseFile.class));
			List<RMReleaseFile> items = query.list();

			return items;

		} catch (Exception e) {
			Sentry.capture(e, "RMRelease");
		} finally {
			sessionObj.close();
		}
		return null;
	}

	@Override
	public RMReleaseFile findByRelease(String release) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RMReleaseFile.class);
		crit.add(Restrictions.eq("release", release));
		return (RMReleaseFile) crit.uniqueResult();
	}

}
