package com.example.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

// @Configuration
// 해당 클래스가 하나 이상의 @Bean 메서드를 가지고 있는 설정 클래스임을 Spring에게 알려줌
// @PropertySource("classpath:/application.properties")
// application.properties 파일에서 설정된 프로퍼티들을 이 클래스에서 사용할 수 있게 해줌
// public class DatabaseConfig {

//     @Autowired
//     private ApplicationContext context ;

//     // @Bean 어노테이션이 붙은 메서드는 Spring 컨테이너에 의해 관리되는 객체(빈)를 생성합니다. 
//     // 이 메서드가 반환하는 객체는 Spring이 애플리케이션에서 필요할 때 자동으로 주입해 줍니다.
//     @Bean
//     @ConfigurationProperties(prefix = "spring.datasource.hikari")
//     public HikariConfig hikariConfig() {
//         return new HikariConfig();
//     }

//     @Bean
//     // 애플리케이션의 데이터베이스 연결을 관리하는 역할
//     public DataSource dataSource() {
//         return new HikariDataSource(hikariConfig());
//     }

//     @Bean
//     // SqlSessionFactory는 MyBatis의 핵심 객체로, SQL 실행, 매핑, 트랜잭션 관리를 담당
//     public SqlSessionFactory sqlSessionFactory() throws Exception {
//         SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//         factoryBean.setDataSource(dataSource());
//         factoryBean.setMapperLocations( context.getResources("classpath:/mappers/**/*Mapper.xml") );
//         factoryBean.setConfigLocation( context.getResource("classpath:/mybatis-config.xml") );
//         return factoryBean.getObject();
//     }

//     @Bean
//     public SqlSessionTemplate sqlSession() throws Exception {
//         return new SqlSessionTemplate(sqlSessionFactory());
//     }
// }