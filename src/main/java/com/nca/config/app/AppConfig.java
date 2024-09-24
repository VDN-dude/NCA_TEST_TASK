package com.nca.config.app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nca.config.audit.AuditorAwareImpl;
import com.nca.config.properties.AppProperties;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * {@code AppConfig} intended to config beans
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nca")
@EnableJpaRepositories(basePackages = "com.nca.repository")
@EnableTransactionManagement
@EnableJpaAuditing
public class AppConfig {

    @Autowired
    private AppProperties appProperties;

    /**
     * {@code AuditorAware} bean for auditing entities.
     * It helps in generating fields depended on time of persistence or updating
     * and also to include person who did this
     * @return {@code AuditorAwareImpl}
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * {@code MappingJackson2HttpMessageConverter} bean for deeper configuration of parsing json values.
     * This configuration allowed single quotes instead of double quotes
     * and also unquoted fields
     * @return {@code MappingJackson2HttpMessageConverter}
     */
    @Bean
    public MappingJackson2HttpMessageConverter convert() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mappingJackson2HttpMessageConverter.setObjectMapper(mapper);

        return mappingJackson2HttpMessageConverter;
    }

    /**
     * {@code DataSource} bean for configure datasource.
     * All values getting from {@code AppProperties}.
     * @return {@code DataSource}
     */
    @Bean
    public DataSource dataSource() {
        AppProperties.Datasource datasource = appProperties.getDatasource();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(datasource.getDriver());
        basicDataSource.setUrl(datasource.getUrl());
        basicDataSource.setUsername(datasource.getUsername());
        basicDataSource.setPassword(datasource.getPassword());

        return basicDataSource;
    }

    /**
     * {@code LocalContainerEntityManagerFactoryBean} bean for configure entity manager factory to work with jpa.
     * @return {@code LocalContainerEntityManagerFactoryBean}
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.nca.entity");
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(hibernateProperties());

        return entityManagerFactory;
    }

    /**
     * {@code PersistenceExceptionTranslationPostProcessor} bean for translating {@code SqlException}.
     * @return {@code PersistenceExceptionTranslationPostProcessor}
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * {@code LocalSessionFactoryBean} bean configure session factory for database.
     * @return {@code LocalSessionFactoryBean}
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setPackagesToScan("com.nca");
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());

        return localSessionFactoryBean;
    }

    /**
     * {@code PlatformTransactionManager} bean configure transaction manager.
     * It gets access to manage transactions manually.
     * @return {@code PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return jpaTransactionManager;
    }

    /**
     * {@code Properties} bean configure properties for hibernate.
     * All values getting from {@code AppProperties}.
     * @return {@code Properties}
     */
    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        AppProperties.Hibernate hibernate = appProperties.getHibernate();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hibernate.getHbm2ddlAuto());
        hibernateProperties.setProperty("hibernate.dialect", hibernate.getDialect());
        hibernateProperties.setProperty("show_sql", hibernate.getShowSql());
        hibernateProperties.setProperty("hibernate.default_schema", hibernate.getDefaultSchema());

        return hibernateProperties;
    }
}
