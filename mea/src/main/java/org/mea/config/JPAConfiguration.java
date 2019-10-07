package org.mea.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class JPAConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws URISyntaxException {

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        factoryBean.setJpaVendorAdapter(vendorAdapter);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        
        //********************MYSQL******************************
        //dataSource.setDriverClassName("com.mysql.jdbc.Driver");
       /* dataSource.setUsername("root");
        dataSource.setPassword("1035491020");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mea?useTimezone=true&serverTimezone=UTC");*/
        
        
        //************************POSTGRE**************************
        URI dbUrl = new URI(env.getProperty("DATABASE_URL"));
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://"+dbUrl.getHost()
        	+":"+dbUrl.getPort()+dbUrl.getPath());
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
        
	}
	
	@Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
	}
}
