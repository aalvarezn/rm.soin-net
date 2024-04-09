package com.soin.sgrm.configuration;

import java.util.Properties;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.soin.sgrm.utils.AppProperties;

import com.soin.sgrm.exception.Sentry;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.soin.sgrm.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;

	public static final Logger logger = Logger.getLogger(HibernateConfiguration.class);

	AppProperties appProperties = new AppProperties();

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.soin.sgrm.model", "com.soin.sgrm.model.wf" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		String path = context.getContextPath();
		JndiTemplate jndiTemplate = new JndiTemplate();
		// --- prd ---
		if (path.contains("sgrmprod")) {
			DataSource dataSource;
			try {
				dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrm");
				return dataSource;
			} catch (NamingException e) {
				Sentry.capture(e, "hibernate");
			}
		} else {
			// --- qa ---
			if (path.contains("sgrm_qa")) {
				DataSource dataSource;
				try {
					dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrm_qa");
					return dataSource;
				} catch (NamingException e) {
					Sentry.capture(e, "hibernate");
				}

			} else {
				if (path.contains("sgrm_demos")) {
					DataSource dataSource;
					try {
						dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrm_demos");
						return dataSource;
					} catch (NamingException e) {
						Sentry.capture(e, "hibernate");
					}
				} else {

					// --- desa ---
					DataSource dataSource;
					try {
						dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgm_desa_pos");
						return dataSource;
					} catch (NamingException e) {
						Sentry.capture(e, "hibernate");
					}
				}
			}
		}
		return null;
	}

	@Bean(name = "transactionManager")
	@Autowired
	public HibernateTransactionManager transactionManager(@Qualifier("sessionFactory") SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	@Bean(name = "sessionFactoryCorp")
	public LocalSessionFactoryBean sessionFactoryCorp() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSourceCorp());
		sessionFactory.setPackagesToScan(new String[] { "com.soin.cpt.model.corp" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}
	
	
	@Bean(name = "dataSourceCorp")
	public DataSource dataSourceCorp() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("ds.corp.database-driver"));
		dataSource.setUrl(env.getProperty("ds.corp.url"));
		dataSource.setUsername(env.getProperty("ds.corp.username"));
		dataSource.setPassword(env.getProperty("ds.corp.password"));
		return dataSource;
	}

	@Bean(name = "transactionManagerCorp")
	@Autowired
	public HibernateTransactionManager transactionManagerCorp(@Qualifier("sessionFactoryCorp") SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	/* DatasourcePostgres */

	@Bean(name = "sessionFactoryPos")
	public LocalSessionFactoryBean sessionFactoryPos() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSourcePos());
		sessionFactory.setPackagesToScan(new String[] { "com.soin.sgrm.model.pos" });
		sessionFactory.setHibernateProperties(hibernatePropertiesPos());
		return sessionFactory;
	}

	@Bean(name = "dataSourcePos")
	public DataSource dataSourcePos() {
		String path = context.getContextPath();
		JndiTemplate jndiTemplate = new JndiTemplate();
		// --- prd ---
		if (path.contains("sgrmprod")) {
			DataSource dataSource;
			try {
				dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrmpos");
				return dataSource;
			} catch (NamingException e) {
				Sentry.capture(e, "hibernate");
			}
		} else {
			// --- qa ---
			if (path.contains("sgrm_qa")) {
				DataSource dataSource;
				try {
					dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrm_qa_pos");
					return dataSource;
				} catch (NamingException e) {
					Sentry.capture(e, "hibernate");
				}

			} else {
				if (path.contains("sgrm_demos")) {
					DataSource dataSource;
					try {
						dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgrm_demos_pos");
						return dataSource;
					} catch (NamingException e) {
						Sentry.capture(e, "hibernate");
					}
				} else {

					// --- desa ---
					DataSource dataSource;
					try {
						dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/sgm_desa_pos");
						return dataSource;
					} catch (NamingException e) {
						Sentry.capture(e, "hibernate");
					}
				}
			}
		}
		return null;
	
	}

	@Bean(name = "transactionManagerPos")
	@Autowired
	public HibernateTransactionManager transactionManagerPos(@Qualifier("sessionFactoryPos") SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}
	
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		String[] _properties = new String[] { "hibernate.dialect", "hibernate.show_sql", "hibernate.SQL",
				"hibernate.format_sql", "hibernate.connection.CharSet", "hibernate.connection.characterEncoding",
				"hibernate.connection.useUnicode" };
		for (int i = 0; i < _properties.length; i++) {
			String property = appProperties.getProperty(_properties[i]);
			if (property != null)
				properties.put(_properties[i], property);
		}
		return properties;
	}
	
	
	private Properties hibernatePropertiesPos() {
		Properties properties = new Properties();
		String[] _properties = new String[] { "hibernate.default_schema", "hibernate.format_sql",
				"hibernate.connection.CharSet", "hibernate.connection.characterEncoding",
				"hibernate.connection.useUnicode", "hibernate.naming.implicit-strategy",
				"hibernate.naming.physical-strategy", "hibernate.globally_quoted_identifiers" };
		for (int i = 0; i < _properties.length; i++) {
			String property = appProperties.getProperty(_properties[i]);
			if (property != null)
				properties.put(_properties[i], property);
		}
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return properties;
	}

}