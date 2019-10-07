package org.mea.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@EnableTransactionManagement
public class JPAConfiguration {
	
	@Autowired
	private Environment environment;
	
	/*@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws URISyntaxException {

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        factoryBean.setJpaVendorAdapter(vendorAdapter);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        
        //********************MYSQL******************************
        //dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("1035491020");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mea?useTimezone=true&serverTimezone=UTC");
        
        
        //************************POSTGRE**************************
        //URI dbUrl = new URI(env.getProperty("DATABASE_URL"));
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        URI dbUrl = new URI(env.getProperty("DATABASE_URL"));
        
        dataSource.setUrl("jdbc:postgresql://"+dbUrl.getHost()+":"+dbUrl.getPort()+dbUrl.getPath());
        dataSource.setUsername(dbUrl.getUserInfo().split(":")[0]);
        dataSource.setPassword(dbUrl.getUserInfo().split(":")[1]);

        factoryBean.setDataSource(dataSource);

        Properties props = new Properties();
      //********************MYSQL******************************
       // props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        
      //************************POSTGRE**************************
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.postgreSQLDialect");
        
      //************************RESTANTE MANTEM**************************
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        
        props.setProperty("hibernate.connection.CharSet", "utf8");
        props.setProperty("hibernate.connection.characterEncoding", "utf8");
        props.setProperty("hibernate.connection.useUnicode", "true");
        
        

        factoryBean.setJpaProperties(props);

        factoryBean.setPackagesToScan("org.mea.models");

        return factoryBean;
        
	}*/
	
	
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource, Properties additionalProperties) {
		LocalContainerEntityManagerFactoryBean factoryBean = 
				new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("org.mea.models");
		factoryBean.setDataSource(dataSource);
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		factoryBean.setJpaProperties(additionalProperties);
		
		return factoryBean;
	}

	@Bean
	@Profile("dev")
	public Properties additionalProperties() {
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		props.setProperty("hibernate.show_sql", "true");
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		return props;
	}

	@Bean
	@Profile("dev")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("1035491020");
		dataSource.setUrl("jdbc:mysql://localhost:3306/mea?useTimezone=true&serverTimezone=UTC");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		
		return dataSource;
	}
	
	@Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
	}
}
