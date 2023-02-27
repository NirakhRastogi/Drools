package com.drools.DroolsDemo;

import org.apache.tomcat.util.digester.Rules;
import org.kie.api.KieBase;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DroolsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroolsDemoApplication.class, args);
	}


}
