package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Tree;


@Repository
public class PTreeDaoImpl implements PTreeDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<Tree> findTree(String releaseNumber, Integer depth) {
		String sql = String.format(
				" SELECT NULL AS FATHER , NULL AS FATHERID, ORIGIN.\"NUMERO_RELEASE\" AS CHILDREN, ORIGIN.\"ID\" AS CHILDRENID, SORIGIN.\"NOMBRE\" AS STATUS, 0 AS \"DEPTHNODE\"   " + 
						" FROM \"RELEASES_RELEASE\" ORIGIN   " + 
						" INNER JOIN \"RELEASES_ESTADO\" SORIGIN   " + 
						" ON SORIGIN.\"ID\" = ORIGIN.\"ESTADO_ID\"   " + 
						" WHERE ORIGIN.\"NUMERO_RELEASE\" = :releaseNumber  " + 
						" UNION ALL  " + 
						" SELECT FATHER.\"NUMERO_RELEASE\",RQ.\"FROM_RELEASE_ID\", CHILDREN.\"NUMERO_RELEASE\", RQ.\"TO_RELEASE_ID\", CORIGIN.\"NOMBRE\" AS STATUS, RQ.\"DEPTHNODE\" FROM (" + 
						" WITH RECURSIVE EXPL ( \"FROM_RELEASE_ID\",\"TO_RELEASE_ID\", \"DEPTHNODE\") AS   " + 
						" (   " + 
						" SELECT  ROOT.\"FROM_RELEASE_ID\", ROOT.\"TO_RELEASE_ID\", 1 AS \"DEPTHNODE\"   " + 
						" FROM   \"RELEASES_RELEASE_DEPENDENCIAS\"   ROOT   " + 
						" INNER JOIN \"RELEASES_RELEASE\" R1   " + 
						" ON R1.\"ID\" = ROOT.\"FROM_RELEASE_ID\"   " + 
						" WHERE  R1.\"NUMERO_RELEASE\" = :releaseNumber  " + 
						" UNION ALL   " + 
						" SELECT  CHILD.\"FROM_RELEASE_ID\", CHILD.\"TO_RELEASE_ID\", \"DEPTHNODE\" + 1   " + 
						" FROM   EXPL PARENT, \"RELEASES_RELEASE_DEPENDENCIAS\" CHILD   " + 
						" WHERE  PARENT.\"TO_RELEASE_ID\" = CHILD.\"FROM_RELEASE_ID\" AND  \"DEPTHNODE\" < :depth " + 
						" )   " + 
						" SELECT   DISTINCT  \"FROM_RELEASE_ID\", \"TO_RELEASE_ID\",  \"DEPTHNODE\"   " + 
						" FROM     EXPL   " + 
						" ORDER BY  \"FROM_RELEASE_ID\", \"TO_RELEASE_ID\") RQ   " + 
						" INNER JOIN \"RELEASES_RELEASE\" FATHER   " + 
						" ON RQ.\"FROM_RELEASE_ID\" = FATHER.\"ID\"   " + 
						" INNER JOIN \"RELEASES_RELEASE\" CHILDREN   " + 
						" ON RQ.\"TO_RELEASE_ID\" = CHILDREN.\"ID\" " + 
						" INNER JOIN \"RELEASES_ESTADO\" CORIGIN   " + 
				" ON CORIGIN.\"ID\" = CHILDREN.\"ESTADO_ID\" " );

		Query query =  sessionFactory.getCurrentSession().createSQLQuery(sql)
				.addScalar("father", new StringType())
				.addScalar("fatherId", new IntegerType())
				.addScalar("children", new StringType())
				.addScalar("childrenId", new IntegerType())
				.addScalar("depthNode", new IntegerType())
				.addScalar("status", new StringType())
				.setResultTransformer(Transformers.aliasToBean(Tree.class));
		query.setString("releaseNumber", releaseNumber);
		query.setInteger("depth", depth);
		List<Tree> list = query.list();
		if (list.isEmpty())
			return null;
		return list;
	}


}
