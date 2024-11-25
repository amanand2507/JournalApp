package com.aman.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// Maven : Build Automation tool, simplify build, manage dependencies
// Build - Validate,Compile,Test,Package,Verify,Install(package in local repository: for use of Dependency, Deploy
// IOC - inversion of control: Spring provides IOC container(stores all the object used in project)
// Application Context - implements the IOC container -  is a way to achieve IOC container
// if there is annotation above the class/Interface/Method/field then IOC container stores all data about that
// Bean : Object in IOC container
// If annotation is mentioned and bean is created then that class/object can be used anywhere in project
// SpringBootApplication : It holds :- @Configuration @EnableAutoConfiguration @ComponentScan
// @EnableAutoConfiguration : it automatically configure the db or server with project when put dependency in POM.xml
// @Component: Scan our application for classes annotated with @Component,/Instantiate them and inject any specified dependencies into them
//@Autowired annotation:  is used to inject the bean automatically.


@SpringBootApplication
@EnableTransactionManagement //To use @Transactional annotation, you need to configure transaction management using this annotation
public class JournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);
	}


	@Bean   //  configuring transaction management
	public PlatformTransactionManager falana(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}
}
