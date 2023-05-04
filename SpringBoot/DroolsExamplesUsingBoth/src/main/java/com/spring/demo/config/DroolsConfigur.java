package com.spring.demo.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfigur {

	private final KieServices kieServices = KieServices.Factory.get();

	/**
	 * A container for all the KieBases of a given KieModule
	 * 
	 * KieContainer is built with the help of other beans including KieFileSystem,
	 * KieModule, and KieBuilder. The buildAll () method invoked on KieBuilder
	 * builds all the resources and ties them to KieBase.
	 */
	/**
	 * KieFileSystem is an in memory file system used to programmatically define the
	 * resources composing a KieModule
	 * 
	 * @return
	 */
	@Bean
	public KieContainer getKieContainer() {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write(ResourceFactory.newClassPathResource("FdinterestRate.drl"));
		KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
		kb.buildAll();
		/**
		 * A KieModule is a container of all the resources necessary to define a set of
		 * KieBases like a pom.xml defining its ReleaseId, a kmodule.xml file declaring
		 * the KieBases names and configurations together with all the KieSession that
		 * can be created from them and all the other files necessary to build the
		 * KieBases themselves
		 */
		KieModule kieModule = kb.getKieModule();
		/**
		 * Creates a new KieContainer wrapping the KieModule with the given ReleaseId
		 */
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

}
