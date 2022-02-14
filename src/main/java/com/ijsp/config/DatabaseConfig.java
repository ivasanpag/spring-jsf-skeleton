package com.ijsp.config;

import lombok.extern.log4j.Log4j2;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.aop.AopInvocationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.ServletContextAware;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Properties;

/**
 * Database config.
 * @author ijsp
 * @since 1.0
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.ijsp.repository")
@Log4j2
public class DatabaseConfig implements ServletContextAware {

    private ServletContext servletContext;

    String dbName;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Entity manager factory entity manager factory.
     *
     * @return the entity manager factory
     * @throws NamingException the naming exception
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() throws NamingException  {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        DataSource dataSource = dataSource();
        em.setDataSource(dataSource);


        dbName = System.getProperty("database.type") == null ? retrieveDatabaseData(dataSource)
                : System.getProperty("database.type");

        em.setPersistenceUnitName("MYDS");
        em.setPersistenceProvider(new HibernatePersistenceProvider());
        em.setPackagesToScan("com.ijsp.model");

        em.setJpaVendorAdapter( new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        em.setJpaDialect(new org.springframework.orm.jpa.vendor.HibernateJpaDialect());

        em.afterPropertiesSet();
        return em.getObject();
    }

    /**
     * Data source
     *
     * @return the data source
     * @throws NamingException the naming exception
     */
    @Bean
    public DataSource dataSource() throws NamingException {
        log.info("AppServer " + servletContext.getServerInfo());

        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName(ConfigUtils.findJndiNameFromApplicationServer(servletContext.getServerInfo()));
        jndiObjectFactoryBean.setCache(true);
        jndiObjectFactoryBean.setProxyInterface(DataSource.class);
        jndiObjectFactoryBean.setLookupOnStartup(true);
        jndiObjectFactoryBean.afterPropertiesSet();
        return (DataSource) jndiObjectFactoryBean.getObject();
    }

    /**
     * Platform transaction manager.
     *
     * @return the platform transaction manager
     * @throws NamingException the naming exception
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws NamingException{
        return new JpaTransactionManager(entityManagerFactory());
    }

    /**
     * Persistence annotation bean post processor.
     *
     * @return the persistence annotation bean post processor
     */
    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    /**
     * Hibernate properties.
     *
     * @return the properties
     */
    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        properties.setProperty("hibernate.default_schema", System.getProperty("database.schema") == null
                ? "MYSCHEMA" : System.getProperty("database.schema"));
        properties.setProperty("hibernate.query.plan_cache_max_size", "16");
        properties.setProperty("hibernate.query.plan_parameter_metadata_max_size", "32");
        properties.setProperty("hibernate.id.new_generator_mappings", "false");
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.setProperty("hibernate.id.optimizer.pooled.preferred", "pooled-lo");
        properties.setProperty("hibernate.jdbc.batch_size","25");
        properties.setProperty("hibernate.order_inserts"  ,"true");
        properties.setProperty("hibernate.order_updates" ,"true");
        properties.setProperty("hibernate.dialect", ConfigUtils.lookupDialect(dbName));
        return properties;
    }

    /**
     * Retrieve database data string.
     *
     * @param dataSource the data source
     * @return database name
     */
    private String retrieveDatabaseData(DataSource dataSource) {
        String dbName = null;
        try {
            dbName = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDatabaseProductName);
            log.info("Database: " + dbName + ". Driver: " + JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDriverName));
        } catch (AopInvocationException | MetaDataAccessException e) {
            log.error(e, e);
        }
        return dbName;
    }
}
