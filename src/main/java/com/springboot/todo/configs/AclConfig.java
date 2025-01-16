package com.springboot.todo.configs;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.springboot.todo.security.enums.Roles;

@PropertySource("classpath:application.properties")
@Configuration
public class AclConfig {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private Environment env;
	
//	@Bean
//	public ResourceDatabasePopulator databasePopulator() {
//		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//		populator.addScript(new ClassPathResource("acl-schema.sql"));
//		populator.execute(dataSource);
//		return populator;
//	}

	@Bean
	public JdbcMutableAclService mutableAclService(DataSource dataSource) {
		return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
	}
	
	@Bean
	public AclAuthorizationStrategyImpl aclAuthorizationStrategy() {
		return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority(Roles.ROLE_ADMIN.toString()));
	}
	
	@Bean
	public SpringCacheBasedAclCache aclCache() {
		ConcurrentMapCache aclCache = new ConcurrentMapCache("acl_cache");
		return new SpringCacheBasedAclCache(aclCache, permissionGrantingStrategy(), aclAuthorizationStrategy());
	}
	
	@Bean
	public PermissionGrantingStrategy permissionGrantingStrategy() {
		return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
	}
	
	@Bean
	public PermissionFactory permissionFactory() {
		return new DefaultPermissionFactory();
	}
	
	@Bean
	public LookupStrategy lookupStrategy() {
		return new BasicLookupStrategy(
				dataSource, 
				aclCache(), 
				aclAuthorizationStrategy(), 
				new ConsoleAuditLogger());
	}
	
//	@Bean
//	public DataSource dataSource() {
//		BasicDataSource basicDataSource = new BasicDataSource();
//		basicDataSource.setUsername(env.getProperty("spring.datasource.username"));
//		basicDataSource.setPassword(env.getProperty("spring.datasource.password"));
//		basicDataSource.setUrl(env.getProperty("spring.datasource.url"));
//		return basicDataSource;
//	}
}
