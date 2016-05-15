/*
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cn.fh.starter.shiro.autoconfigure;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
@Import(ShiroManager.class)
public class ShiroAutoConfiguration {
    private static Logger log = LoggerFactory.getLogger(ShiroAutoConfiguration.class);

	@Autowired
	private ShiroProperties properties;

	@Bean(name = "realm")
	@DependsOn("lifecycleBeanPostProcessor")
	@ConditionalOnMissingBean
	public Realm realm() {
		Class<?> relmClass = properties.getRealm();
		return (Realm) BeanUtils.instantiate(relmClass);
	}

	@Bean(name = "shiroFilter")
	@DependsOn("securityManager")
	@ConditionalOnMissingBean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultSecurityManager securityManager, Realm realm, ShiroFilterRegistry registry) {
		securityManager.setRealm(realm);

        Map<String, String> filterDef = swapKeyValue(properties.getFilterChainDefinitions());
        log.info("过虑器配置: {}", filterDef);
        log.info("自定义过虑器: {}", registry.getFilterMap());

		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		shiroFilter.setLoginUrl(properties.getLoginUrl());
		shiroFilter.setSuccessUrl(properties.getSuccessUrl());
		shiroFilter.setUnauthorizedUrl(properties.getUnauthorizedUrl());

		shiroFilter.setFilterChainDefinitionMap(filterDef);
        shiroFilter.getFilters().putAll(registry.getFilterMap());

		return shiroFilter;
	}

    private Map<String, String> swapKeyValue(Map<String, String> originalMap) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }

        return map;
    }
}
