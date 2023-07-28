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
import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PCrontab;
import com.soin.sgrm.model.pos.PDetailButtonCommand;

@Repository
public class PCrontabDaoImpl implements PCrontabDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public PCrontab saveCrontab(PCrontab crontab) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			PButtonCommand button = crontab.getButton();
			crontab.updateButton(new PButtonCommand());

			// primero guardo el boton luego los detalles y hago temp
			List<PDetailButtonCommand> temp = button.getDetailsButtonCommands();
			button.updateDetailsButtonCommands(new ArrayList<PDetailButtonCommand>());

			sessionObj.saveOrUpdate(button);
			for (PDetailButtonCommand detailButtonCommand : temp) {
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
	public PCrontab updateCrontab(PCrontab crontab, PButtonCommand old) {
		Transaction transObj = null;
		Session sessionObj = null;
		PButtonCommand button = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			button = crontab.getButton();
			crontab.updateButton(new PButtonCommand());
			for (PDetailButtonCommand detail : button.getDetailsButtonCommands()) {
				detail.setButton(button);
			}

			for (PDetailButtonCommand detailOld : old.getDetailsButtonCommands()) {
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
	public PCrontab findById(Integer id) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PCrontab.class);
		crit.add(Restrictions.eq("id", id));
		return (PCrontab) crit.uniqueResult();
	}
	
	

	@Override
	public void deleteCrontab(PCrontab crontab) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			PButtonCommand button = crontab.getButton();
			for (PDetailButtonCommand detail : crontab.getButton().getDetailsButtonCommands()) {
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

	@Override
	public PCrontab findByIdButton(Integer id) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PCrontab.class);
		crit.add(Restrictions.eq("button.id", id));
		return (PCrontab) crit.uniqueResult();
	}

}
