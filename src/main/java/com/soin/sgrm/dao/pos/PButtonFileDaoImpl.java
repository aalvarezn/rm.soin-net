package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PButtonFile;
import com.soin.sgrm.model.pos.PDetailButtonFile;

@Repository
public class PButtonFileDaoImpl implements PButtonFileDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;


	@Override
	public PButtonFile saveButton(PButtonFile button) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			// primero guardo el boton luego los detalles y hago temp
			List<PDetailButtonFile> temp = button.getDetailsButtonFiles();
			button.updateDetailsButtonFiles(new ArrayList<PDetailButtonFile>());

			sessionObj.saveOrUpdate(button);
			for (PDetailButtonFile detailButtonFile : temp) {
				detailButtonFile.setButton(button);
				sessionObj.saveOrUpdate(detailButtonFile);
			}
			button.updateDetailsButtonFiles(temp);

			transObj.commit();
			return button;
		} catch (Exception e) {
			Sentry.capture(e, "buttonFile");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PButtonFile updateButton(PButtonFile button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		PButtonFile old = findById(button.getId());
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDetailButtonFile detail : button.getDetailsButtonFiles()) {
				detail.setButton(button);
			}

			for (PDetailButtonFile detailOld : old.getDetailsButtonFiles()) {
				if (!button.existDetail(detailOld.getId())) {
					sessionObj.delete(detailOld);
				}
			}
			sessionObj.saveOrUpdate(button);
			transObj.commit();
			return button;
		} catch (Exception e) {
			Sentry.capture(e, "buttonFile");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PButtonFile findById(Integer id) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PButtonFile.class);
		crit.add(Restrictions.eq("id", id));
		return (PButtonFile) crit.uniqueResult();
	}

	@Override
	public void deleteButton(PButtonFile button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDetailButtonFile detail : button.getDetailsButtonFiles()) {
				sessionObj.delete(detail);
			}
			sessionObj.delete(button);
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "buttonFile");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}
