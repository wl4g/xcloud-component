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
package com.wl4g.component.rpc.springboot.feign;

import com.google.gson.Gson;
import com.wl4g.component.rpc.springboot.feign.GithubService1.GitHubContributor;
import com.wl4g.component.rpc.springboot.feign.GithubService2.GitHubRepo;
import com.wl4g.component.rpc.springboot.feign.annotation.EnableSpringBootFeignClients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test1")
@SpringBootTest(classes = SpringBootTests.class)
@EnableSpringBootFeignClients(basePackages = "com.wl4g.component.rpc.springboot.feign")
@EnableAutoConfiguration
public class SpringBootTests {

	private Logger log = LoggerFactory.getLogger(SpringBootTests.class);

	@Autowired
	private GithubService1 githubService1;

	@Autowired
	private GithubService2 githubService2;

	@Test
	public void test1() {
		List<GitHubContributor> contributors = githubService1.getContributors("wl4g", "xcloud-components");
		log.info(">>> Result:");
		log.info("contributors={}", new Gson().toJson(contributors));
	}

	@Test
	public void test2() {
		List<GitHubRepo> repos = githubService2.getRepos("wl4g");
		log.info(">>> Result:");
		log.info("repos={}", new Gson().toJson(repos));
	}

}