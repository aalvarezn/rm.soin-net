package com.soin.sgrm.dao;

import java.awt.Button;
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
import com.soin.sgrm.model.Crontab;
import com.soin.sgrm.model.DetailButtonCommand;

@Repository
public class CrontabDaoImpl implements CrontabDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Crontab saveCrontab(Crontab crontab) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			ButtonCommand button = crontab.getButton();
			crontab.updateButton(new ButtonCommand());

			// primero guardo el boton luego los detalles y hago temp
			List<DetailButtonCommand> temp = button.getDetailsButtonCommands();
			button.updateDetailsButtonCommands(new ArrayList<DetailButtonCommand>());

			sessionObj.saveOrUpdate(button);
			for (DetailButtonCommand detailButtonCommand : temp) {
				detailButtonCommand.setButton(button);
				sessionObj.saveOrUpdate(detailButtonCommand);
			}
			button.updateDetailsButtonCommands(temp);
//			button.setHaveCrontab(true);
			crontab.updateButton(button);

			sessionObj.saveOrUpdate(crontab);
			transObj.commit();
			return crontab;
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public Crontab updateCrontab(Crontab crontab, ButtonCommand old) {
		Transaction transObj = null;
		Session sessionObj = null;
		ButtonCommand button = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			button = crontab.getButton();
			crontab.updateButton(new ButtonCommand());
			for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
				detail.setButton(button);
			}

			for (DetailButtonCommand detailOld : old.getDetailsButtonCommands()) {
				if (!button.existDetail(detailOld.getId())) {
					sessionObj.delete(detailOld);
				}
			}
			sessionObj.saveOrUpdate(button);
			crontab.updateButton(button);
			sessionObj.saveOrUpdate(crontab);
			transObj.commit();
			return crontab;
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public Crontab findById(Integer id) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Crontab.class);
		crit.add(Restrictions.eq("id", id));
		return (Crontab) crit.uniqueResult();
	}

	@Override
	public void deleteCrontab(Crontab crontab) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			ButtonCommand button = crontab.getButton();
			for (DetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
				sessionObj.delete(detail);
			}
			sessionObj.delete(crontab);
			sessionObj.delete(button);
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "crontab");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}
