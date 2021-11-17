package com.soin.sgrm.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;

@Repository
public class ReleaseObjectDaoImpl implements ReleaseObjectDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(rObj);
			sql = String.format(
					"INSERT INTO RELEASES_RELEASE_OBJETOS ( id, release_id, objeto_id) VALUES ( null, %s, %s ) ",
					release.getId(), rObj.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
			return rObj;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteObject(Integer releaseId, ReleaseObject object) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sql = String.format("DELETE FROM releases_release_objetos WHERE release_id = %s AND objeto_id = %s ",
					releaseId, object.getId());

			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(object);

			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public ReleaseObjectEdit findById(Integer id) {
		return (ReleaseObjectEdit) sessionFactory.getCurrentSession().get(ReleaseObjectEdit.class, id);
	}

	@Override
	public Release findReleaseToAddByObject(ReleaseObjectEdit obj, Release release) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release.class);
		crit.setMaxResults(1);

		crit.createAlias("objects", "objects");
		crit.createAlias("status", "status");
		crit.createAlias("objects.configurationItem", "itemConf");
		crit.createAlias("objects.typeObject", "typeObject");

		crit.add(Restrictions.ne("id", release.getId()));
		crit.add(Restrictions.eq("objects.name", obj.getName()));
		crit.add(Restrictions.ne("status.name", "Anulado"));
		crit.add(Restrictions.eq("itemConf.id", obj.getItemConfiguration()));
		crit.add(Restrictions.eq("typeObject.id", obj.getTypeObject()));
		crit.add(Restrictions.lt("createDate", release.getCreateDate()));

		crit.addOrder(Order.desc("createDate"));
		Release releaseDep = (Release) crit.uniqueResult();
		return releaseDep;
	}

	@Override
	public Release findReleaseToDeleteByObject(Release release, ReleaseObject obj) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Release.class);
		crit.setMaxResults(1);

		crit.createAlias("objects", "objects");
		crit.createAlias("status", "status");
		crit.createAlias("objects.configurationItem", "itemConf");
		crit.createAlias("objects.typeObject", "typeObject");

		crit.add(Restrictions.ne("id", release.getId()));
		crit.add(Restrictions.eq("objects.name", obj.getName()));
		crit.add(Restrictions.ne("status.name", "Anulado"));
		crit.add(Restrictions.eq("itemConf.id", obj.getConfigurationItem().getId()));
		crit.add(Restrictions.eq("typeObject.id", obj.getTypeObject().getId()));
		crit.add(Restrictions.lt("createDate", release.getCreateDate()));

		crit.addOrder(Order.desc("createDate"));
		Release releaseDependency = (Release) crit.uniqueResult();
		return releaseDependency;
	}

}
