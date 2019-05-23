/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.skipper.openshift;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.deployer.resource.docker.DockerResource;
import org.springframework.cloud.deployer.resource.maven.MavenResource;
import org.springframework.cloud.deployer.resource.support.DelegatingResourceLoader;
import org.springframework.cloud.deployer.spi.app.AppDeployer;
import org.springframework.cloud.deployer.spi.openshift.OpenShiftDeployerProperties;
import org.springframework.cloud.deployer.spi.openshift.ResourceAwareOpenShiftAppDeployer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OpenShiftSkipperServer.class, properties = {
		"spring.cloud.deployer.openshift.forceBuild=true",
		"spring.cloud.deployer.openshift.limits.memory=128Mi" })
public class OpenShiftDataFlowSkipperTest {

	@Autowired
	private AppDeployer appDeployer;

	@Autowired
	private ApplicationContext context;

	@Test
	public void contextLoads() {
		assertThat(appDeployer).isInstanceOf(ResourceAwareOpenShiftAppDeployer.class);
	}

	@Test
	public void testDeployerProperties() {
		OpenShiftDeployerProperties properties = context
				.getBean(OpenShiftDeployerProperties.class);
		assertThat(properties.isForceBuild()).isTrue();
		assertThat(properties.getLimits().getMemory()).isEqualTo("128Mi");
	}

	@Test
	public void testDockerResource() {
		DelegatingResourceLoader resourceLoader = context
				.getBean(DelegatingResourceLoader.class);
		assertThat(resourceLoader.getResource(
				"maven://org.springframework.cloud:spring-cloud-dataflow-server-core:1.2.3.RELEASE"))
						.isInstanceOf(MavenResource.class);
		assertThat(resourceLoader.getResource("docker://helloworld:latest"))
				.isInstanceOf(DockerResource.class);
	}

}
