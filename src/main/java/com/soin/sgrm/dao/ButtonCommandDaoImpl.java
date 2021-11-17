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

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.DetailButtonCommand;

@Repository
public class ButtonCommandDaoImpl implements ButtonCommandDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public ButtonCommand saveButton(ButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			// primero guardo el boton luego los detalles y hago temp
			List<DetailButtonCommand> temp = button.getDetailsButtonCommands();
			button.updateDetailsButtonCommands(new ArrayList<DetailButtonCommand>());

			sessionObj.saveOrUpdate(button);
			for (DetailButtonCommand detailButtonCommand : temp) {
				detailButtonCommand.setButton(button);
				sessionObj.saveOrUpdate(detailButtonCommand);
			}
			button.updateDetailsButtonCommands(temp);

			transObj.commit();
			return button;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteButton(ButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
				sessionObj.delete(detail);
			}
			sessionObj.delete(button);
			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public ButtonCommand findById(Integer id) throws SQLException {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(ButtonCommand.class);
		cri.add(Restrictions.eq("id", id));
		return (ButtonCommand) cri.uniqueResult();
	}

	@Override
	public ButtonCommand updateButton(ButtonCommand button) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		ButtonCommand old = findById(button.getId());
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (DetailButtonCommand detail : button.getDetailsButtonCommands()) {
				detail.setButton(button);
			}
			
			for (DetailButtonCommand detailOld : old.getDetailsButtonCommands()) {
				if(!button.existDetail(detailOld.getId())) {
					sessionObj.delete(detailOld);
				}
			}
			sessionObj.saveOrUpdate(button);
			transObj.commit();
			return button;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}
