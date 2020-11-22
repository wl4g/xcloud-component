/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.config.spring.beans.factory.annotation;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.AnnotationPropertyValuesAdapter;
import com.alibaba.dubbo.config.spring.context.annotation.DubboClassPathBeanDefinitionScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.alibaba.dubbo.config.spring.util.ObjectUtils.of;
import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ClassUtils.resolveClassName;

/**
 * {@link FeignClientToDubboProviderConfigurer}
 * 
 * @author Wangl.sir &lt;wanglsir@gmail.com, 983708408@qq.com&gt;
 * @version v1.0 2020-11-20
 * @sine v1.0
 * @see {@link com.alibaba.dubbo.config.spring.beans.factory.annotation.ServiceAnnotationBeanPostProcessor}
 * @see {@link com.alibaba.dubbo.config.spring.ServiceBean}
 * @see {@link com.alibaba.dubbo.config.spring.ReferenceBean}
 */
public class FeignClientToDubboProviderConfigurer
		implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ResourceLoaderAware, BeanClassLoaderAware {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Set<String> packagesToScan;
	private Environment environment;
	private ResourceLoader resourceLoader;
	private ClassLoader classLoader;
	private Service defaultService;

	public FeignClientToDubboProviderConfigurer(String... packagesToScan) {
		this(asList(packagesToScan));
	}

	public FeignClientToDubboProviderConfigurer(Collection<String> packagesToScan) {
		this(new LinkedHashSet<String>(packagesToScan));
	}

	public FeignClientToDubboProviderConfigurer(Set<String> packagesToScan) {
		this.packagesToScan = packagesToScan;
		// Generate {@code @Service} default configuration instance.
		@Service
		final class DefaultServiceClass {
		}
		this.defaultService = DefaultServiceClass.class.getAnnotation(Service.class);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		Set<String> resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);
		if (!CollectionUtils.isEmpty(resolvedPackagesToScan)) {
			registerServiceBeans(resolvedPackagesToScan, registry);
		} else {
			log.warn("packagesToScan is empty , ServiceBean registry will be ignored!");
		}
	}

	/**
	 * Registers Beans whose classes was annotated {@link FeignClient}
	 *
	 * @param packagesToScan
	 *            The base packages to scan
	 * @param registry
	 *            {@link BeanDefinitionRegistry}
	 */
	private void registerServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
		DubboClassPathBeanDefinitionScanner scanner = new DubboClassPathBeanDefinitionScanner(registry, environment,
				resourceLoader);
		BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
		scanner.setBeanNameGenerator(beanNameGenerator);
		scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class, true, true));

		for (String s : registry.getBeanDefinitionNames()) {
			System.out.println(s);
		}

		for (String packageToScan : packagesToScan) {
			// Registers @Service Bean first
			scanner.scan(packageToScan);

			// Finds all BeanDefinitionHolders of @Service whether
			// @ComponentScan scans or not.
			Set<BeanDefinitionHolder> beanDefinitionHolders = findServiceBeanDefinitionHolders(scanner, packageToScan, registry,
					beanNameGenerator);
			if (!CollectionUtils.isEmpty(beanDefinitionHolders)) {
				for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
					registerServiceBean(beanDefinitionHolder, registry, scanner);
				}
				log.info(beanDefinitionHolders.size() + " annotated Dubbo's @Service Components { " + beanDefinitionHolders
						+ " } were scanned under package[" + packageToScan + "]");
			} else {
				log.warn("No Spring Bean annotating Dubbo's @Service was found under package[" + packageToScan + "]");
			}
		}
	}

	/**
	 * It'd better to use BeanNameGenerator instance that should reference
	 * {@link ConfigurationClassPostProcessor#componentScanBeanNameGenerator},
	 * thus it maybe a potential problem on bean name generation.
	 *
	 * @param registry
	 *            {@link BeanDefinitionRegistry}
	 * @return {@link BeanNameGenerator} instance
	 * @see SingletonBeanRegistry
	 * @see AnnotationConfigUtils#CONFIGURATION_BEAN_NAME_GENERATOR
	 * @see ConfigurationClassPostProcessor#processConfigBeanDefinitions
	 * @since 2.5.8
	 */
	private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {
		BeanNameGenerator beanNameGenerator = null;
		if (registry instanceof SingletonBeanRegistry) {
			SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(registry);
			beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);
		}

		if (beanNameGenerator == null) {
			log.info(
					"BeanNameGenerator bean can't be found in BeanFactory with name [" + CONFIGURATION_BEAN_NAME_GENERATOR + "]");
			log.info("BeanNameGenerator will be a instance of " + AnnotationBeanNameGenerator.class.getName()
					+ " , it maybe a potential problem on bean name generation.");
			beanNameGenerator = new AnnotationBeanNameGenerator();
		}
		return beanNameGenerator;
	}

	/**
	 * Finds a {@link Set} of {@link BeanDefinitionHolder BeanDefinitionHolders}
	 * whose bean type annotated {@link Service} Annotation.
	 *
	 * @param scanner
	 *            {@link ClassPathBeanDefinitionScanner}
	 * @param packageToScan
	 *            pachage to scan
	 * @param registry
	 *            {@link BeanDefinitionRegistry}
	 * @return non-null
	 * @since 2.5.8
	 */
	private Set<BeanDefinitionHolder> findServiceBeanDefinitionHolders(ClassPathBeanDefinitionScanner scanner,
			String packageToScan, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
		Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);

		Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>(beanDefinitions.size());
		for (BeanDefinition beanDefinition : beanDefinitions) {
			String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
			BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
			beanDefinitionHolders.add(beanDefinitionHolder);
		}
		return beanDefinitionHolders;
	}

	/**
	 * Registers {@link ServiceBean} from new annotated {@link Service}
	 * {@link BeanDefinition}
	 *
	 * @param beanDefinitionHolder
	 * @param registry
	 * @param scanner
	 * @see ServiceBean
	 * @see BeanDefinition
	 */
	private void registerServiceBean(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry registry,
			DubboClassPathBeanDefinitionScanner scanner) {
		Class<?> beanClass = resolveClass(beanDefinitionHolder);
		Service service = findAnnotation(beanClass, Service.class);
		if (null == service) {
			service = this.defaultService;
		}

		Class<?> interfaceClass = resolveServiceInterfaceClass(beanClass, service);
		String annotatedServiceBeanName = beanDefinitionHolder.getBeanName();
		AbstractBeanDefinition serviceBeanDefinition = buildServiceBeanDefinition(service, interfaceClass,
				annotatedServiceBeanName);

		// ServiceBean Bean name
		String beanName = generateServiceBeanName(service, interfaceClass, annotatedServiceBeanName);

		// check duplicated candidate bean
		if (scanner.checkCandidate(beanName, serviceBeanDefinition)) {
			registry.registerBeanDefinition(beanName, serviceBeanDefinition);
			log.warn("The BeanDefinition[" + serviceBeanDefinition + "] of ServiceBean has been registered with name : "
					+ beanName);
		} else {
			log.warn("The Duplicated BeanDefinition[" + serviceBeanDefinition + "] of ServiceBean[ bean name : " + beanName
					+ "] was be found , Did @DubboComponentScan scan to same package in many times?");
		}
	}

	/**
	 * Generates the bean name of {@link ServiceBean}
	 *
	 * @param service
	 * @param interfaceClass
	 *            the class of interface annotated {@link Service}
	 * @param annotatedServiceBeanName
	 *            the bean name of annotated {@link Service}
	 * @return ServiceBean@interfaceClassName#annotatedServiceBeanName
	 * @since 2.5.9
	 */
	private String generateServiceBeanName(Service service, Class<?> interfaceClass, String annotatedServiceBeanName) {
		StringBuilder beanNameBuilder = new StringBuilder(ServiceBean.class.getSimpleName());
		beanNameBuilder.append(SEPARATOR).append(annotatedServiceBeanName);

		String interfaceClassName = interfaceClass.getName();
		beanNameBuilder.append(SEPARATOR).append(interfaceClassName);

		String version = service.version();
		if (StringUtils.hasText(version)) {
			beanNameBuilder.append(SEPARATOR).append(version);
		}

		String group = service.group();
		if (StringUtils.hasText(group)) {
			beanNameBuilder.append(SEPARATOR).append(group);
		}

		return beanNameBuilder.toString();
	}

	private Class<?> resolveServiceInterfaceClass(Class<?> annotatedServiceBeanClass, Service service) {
		Class<?> interfaceClass = service.interfaceClass();
		if (void.class.equals(interfaceClass)) {
			interfaceClass = null;
			String interfaceClassName = service.interfaceName();
			if (StringUtils.hasText(interfaceClassName)) {
				if (ClassUtils.isPresent(interfaceClassName, classLoader)) {
					interfaceClass = resolveClassName(interfaceClassName, classLoader);
				}
			}
		}

		if (interfaceClass == null) {
			Class<?>[] allInterfaces = annotatedServiceBeanClass.getInterfaces();
			if (allInterfaces.length > 0) {
				interfaceClass = allInterfaces[0];
			}
		}

		Assert.notNull(interfaceClass, "@Service interfaceClass() or interfaceName() or interface class must be present!");
		Assert.isTrue(interfaceClass.isInterface(), "The type that was annotated @Service is not an interface!");
		return interfaceClass;
	}

	private Class<?> resolveClass(BeanDefinitionHolder beanDefinitionHolder) {
		BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
		return resolveClass(beanDefinition);
	}

	private Class<?> resolveClass(BeanDefinition beanDefinition) {
		String beanClassName = beanDefinition.getBeanClassName();
		return resolveClassName(beanClassName, classLoader);
	}

	private Set<String> resolvePackagesToScan(Set<String> packagesToScan) {
		Set<String> resolvedPackagesToScan = new LinkedHashSet<String>(packagesToScan.size());
		for (String packageToScan : packagesToScan) {
			if (StringUtils.hasText(packageToScan)) {
				String resolvedPackageToScan = environment.resolvePlaceholders(packageToScan.trim());
				resolvedPackagesToScan.add(resolvedPackageToScan);
			}
		}
		return resolvedPackagesToScan;
	}

	private AbstractBeanDefinition buildServiceBeanDefinition(Service service, Class<?> interfaceClass,
			String annotatedServiceBeanName) {
		BeanDefinitionBuilder builder = rootBeanDefinition(ServiceBean.class);
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		String[] ignoreAttributeNames = of("provider", "monitor", "application", "module", "registry", "protocol", "interface");
		propertyValues.addPropertyValues(new AnnotationPropertyValuesAdapter(service, environment, ignoreAttributeNames));

		// References "ref" property to annotated-@Service Bean
		addPropertyReference(builder, "ref", annotatedServiceBeanName);
		// Set interface
		builder.addPropertyValue("interface", interfaceClass.getName());

		/**
		 * Add {@link com.alibaba.dubbo.config.ProviderConfig} Bean reference
		 */
		String providerConfigBeanName = service.provider();
		if (StringUtils.hasText(providerConfigBeanName)) {
			addPropertyReference(builder, "provider", providerConfigBeanName);
		}

		/**
		 * Add {@link com.alibaba.dubbo.config.MonitorConfig} Bean reference
		 */
		String monitorConfigBeanName = service.monitor();
		if (StringUtils.hasText(monitorConfigBeanName)) {
			addPropertyReference(builder, "monitor", monitorConfigBeanName);
		}

		/**
		 * Add {@link com.alibaba.dubbo.config.ApplicationConfig} Bean reference
		 */
		String applicationConfigBeanName = service.application();
		if (StringUtils.hasText(applicationConfigBeanName)) {
			addPropertyReference(builder, "application", applicationConfigBeanName);
		}

		/**
		 * Add {@link com.alibaba.dubbo.config.ModuleConfig} Bean reference
		 */
		String moduleConfigBeanName = service.module();
		if (StringUtils.hasText(moduleConfigBeanName)) {
			addPropertyReference(builder, "module", moduleConfigBeanName);
		}

		/**
		 * Add {@link com.alibaba.dubbo.config.RegistryConfig} Bean reference
		 */
		String[] registryConfigBeanNames = service.registry();
		List<RuntimeBeanReference> registryRuntimeBeanReferences = toRuntimeBeanReferences(registryConfigBeanNames);
		if (!registryRuntimeBeanReferences.isEmpty()) {
			builder.addPropertyValue("registries", registryRuntimeBeanReferences);
		}

		/**
		 * Add {@link com.alibaba.dubbo.config.ProtocolConfig} Bean reference
		 */
		String[] protocolConfigBeanNames = service.protocol();
		List<RuntimeBeanReference> protocolRuntimeBeanReferences = toRuntimeBeanReferences(protocolConfigBeanNames);

		if (!protocolRuntimeBeanReferences.isEmpty()) {
			builder.addPropertyValue("protocols", protocolRuntimeBeanReferences);
		}

		return builder.getBeanDefinition();
	}

	private ManagedList<RuntimeBeanReference> toRuntimeBeanReferences(String... beanNames) {
		ManagedList<RuntimeBeanReference> runtimeBeanReferences = new ManagedList<RuntimeBeanReference>();
		if (!ObjectUtils.isEmpty(beanNames)) {
			for (String beanName : beanNames) {
				String resolvedBeanName = environment.resolvePlaceholders(beanName);
				runtimeBeanReferences.add(new RuntimeBeanReference(resolvedBeanName));
			}
		}
		return runtimeBeanReferences;
	}

	private void addPropertyReference(BeanDefinitionBuilder builder, String propertyName, String beanName) {
		String resolvedBeanName = environment.resolvePlaceholders(beanName);
		builder.addPropertyReference(propertyName, resolvedBeanName);
	}

	private static final String SEPARATOR = ":";

}