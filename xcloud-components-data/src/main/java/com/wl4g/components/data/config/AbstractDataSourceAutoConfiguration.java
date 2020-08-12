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
package com.wl4g.components.data.config;

import static com.wl4g.components.common.log.SmartLoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.github.pagehelper.PageHelper;
import com.wl4g.components.common.log.SmartLogger;
import com.wl4g.components.data.mybatis.session.MultipleSqlSessionFactoryBean;

/**
 * {@link AbstractDataSourceAutoConfiguration}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-08-05
 * @since
 */
public abstract class AbstractDataSourceAutoConfiguration {

	protected final SmartLogger log = getLogger(getClass());

	@Autowired
	protected Environment environment;

	/**
	 * New create {@link MultiSqlSessionFactoryBean} instance
	 * 
	 * @param dataSource
	 * @param config
	 * @return
	 * @throws Exception
	 */
	protected SqlSessionFactoryBean createMultiSqlSessionFactoryBean(DataSource dataSource, MybatisProperties config)
			throws Exception {
		// Define path matcher resolver.
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		// SqlSessionFactory
		SqlSessionFactoryBean factory = new MultipleSqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTypeAliases(getTypeAliases(resolver, config));
		factory.setConfigLocation(new ClassPathResource(config.getConfigLocation()));

		// Page.
		PageHelper pageHelper = new PageHelper();
		Properties props = new Properties();
		props.setProperty("dialect", "mysql");
		props.setProperty("reasonable", "true");
		props.setProperty("supportMethodsArguments", "true");
		props.setProperty("returnPageInfo", "check");
		props.setProperty("params", "count=countSql");
		pageHelper.setProperties(props); // 添加插件
		factory.setPlugins(new Interceptor[] { pageHelper });

		factory.setMapperLocations(resolver.getResources(config.getMapperLocations()));
		return factory;
	}

	/**
	 * Let typeAliasesPackage alias bean support wildcards.
	 * 
	 * @return
	 */
	protected Class<?>[] getTypeAliases(PathMatchingResourcePatternResolver resolver, MybatisProperties config) throws Exception {
		List<Class<?>> typeAliases = new ArrayList<>();

		// Define metadataReader
		MetadataReaderFactory metadataReaderFty = new CachingMetadataReaderFactory(resolver);

		for (String pkg : config.getTypeAliasesPackage().split(",")) {
			// Get location
			String location = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg)
					+ "**/*.class";
			// Get resources.
			Resource[] resources = resolver.getResources(location);
			if (resources != null) {
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader metadataReader = metadataReaderFty.getMetadataReader(resource);
						typeAliases.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
					}
				}
			}
		}

		return typeAliases.toArray(new Class<?>[] {});
	}

}