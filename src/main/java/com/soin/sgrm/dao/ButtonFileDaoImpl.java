package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.ButtonFile;
import com.soin.sgrm.model.DetailButtonCommand;
import com.soin.sgrm.model.DetailButtonFile;

@Repository
public class ButtonFileDaoImpl implements ButtonFileDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public ButtonFile saveButton(ButtonFile button) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			// primero guardo el boton luego los detalles y hago temp
			List<DetailButtonFile> temp = button.getDetailsButtonFiles();
			button.updateDetailsButtonFiles(new ArrayList<DetailButtonFile>());

			sessionObj.saveOrUpdate(button);
			for (DetailButtonFile detailButtonFile : temp) {
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
	public ButtonFile updateButton(ButtonFile button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		ButtonFile old = findById(button.getId());
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (DetailButtonFile detail : button.getDetailsButtonFiles()) {
				detail.setButton(button);
			}

			for (DetailButtonFile detailOld : old.getDetailsButtonFiles()) {
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
	public ButtonFile findById(Integer id) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ButtonFile.class);
		crit.add(Restrictions.eq("id", id));
		return (ButtonFile) crit.uniqueResult();
	}

	@Override
	public void deleteButton(ButtonFile button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (DetailButtonFile detail : button.getDetailsButtonFiles()) {
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
