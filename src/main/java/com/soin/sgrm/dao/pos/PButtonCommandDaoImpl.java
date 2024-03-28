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
import com.soin.sgrm.model.pos.PDetailButtonCommand;


@Repository
public class PButtonCommandDaoImpl implements PButtonCommandDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public PButtonCommand saveButton(PButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			// primero guardo el boton luego los detalles y hago temp
			List<PDetailButtonCommand> temp = button.getDetailsButtonCommands();
			button.updateDetailsButtonCommands(new ArrayList<PDetailButtonCommand>());

			sessionObj.saveOrUpdate(button);
			for (PDetailButtonCommand detailButtonCommand : temp) {
				detailButtonCommand.setButton(button);
				sessionObj.saveOrUpdate(detailButtonCommand);
			}
			button.updateDetailsButtonCommands(temp);

			transObj.commit();
			return button;
		} catch (Exception e) {
			Sentry.capture(e, "buttonCommand");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteButton(PButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDetailButtonCommand detail : button.getDetailsButtonCommands()) {
				sessionObj.delete(detail);
			}
			sessionObj.delete(button);
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "buttonCommand");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PButtonCommand findById(Integer id) throws SQLException {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(PButtonCommand.class);
		cri.add(Restrictions.eq("id", id));
		return (PButtonCommand) cri.uniqueResult();
	}

	@Override
	public PButtonCommand updateButton(PButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		PButtonCommand old = findById(button.getId());
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDetailButtonCommand detail : button.getDetailsButtonCommands()) {
				detail.setButton(button);
			}
			
			for (PDetailButtonCommand detailOld : old.getDetailsButtonCommands()) {
				if(!button.existDetail(detailOld.getId())) {
					sessionObj.delete(detailOld);
				}
			}
			sessionObj.saveOrUpdate(button);
			transObj.commit();
			return button;
		} catch (Exception e) {
			Sentry.capture(e, "buttonCommand");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}
