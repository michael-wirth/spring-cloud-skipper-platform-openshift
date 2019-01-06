/*
 * Copyright 2018-2019 the original author or authors.
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

package org.springframework.cloud.skipper.deployer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.deployer.resource.maven.MavenProperties;
import org.springframework.cloud.deployer.spi.kubernetes.ContainerFactory;
import org.springframework.cloud.deployer.spi.openshift.OpenShiftAppDeployer;
import org.springframework.cloud.deployer.spi.openshift.OpenShiftDeployerProperties;
import org.springframework.cloud.deployer.spi.openshift.ResourceAwareOpenShiftAppDeployer;
import org.springframework.cloud.deployer.spi.openshift.ResourceHash;
import org.springframework.cloud.deployer.spi.openshift.maven.MavenOpenShiftAppDeployer;
import org.springframework.cloud.deployer.spi.openshift.maven.MavenResourceJarExtractor;
import org.springframework.cloud.deployer.spi.openshift.resources.pod.OpenShiftContainerFactory;
import org.springframework.cloud.deployer.spi.openshift.resources.volumes.VolumeMountFactory;
import org.springframework.cloud.skipper.domain.Deployer;
import org.springframework.cloud.skipper.domain.Platform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cloud Skipper autoconfiguration for the Openshift platform.
 *
 * @author Donovan Muller
 * @author Michael Wirth
 */
@Configuration
@EnableConfigurationProperties(OpenShiftPlatformProperties.class)
public class OpenShiftPlatformAutoConfiguration {

	private static final Logger log = LoggerFactory
			.getLogger(OpenShiftPlatformAutoConfiguration.class);

	@Bean
	public Platform deployers(OpenShiftPlatformProperties platformProperties,
			MavenResourceJarExtractor mavenResourceJarExtractor,
			MavenProperties mavenProperties, ResourceHash resourceHash) {
		List<Deployer> deployers = new ArrayList<>();
		Map<String, OpenShiftDeployerProperties> openShiftDeployerPropertiesMap = platformProperties
				.getAccounts();
		for (Map.Entry<String, OpenShiftDeployerProperties> entry : openShiftDeployerPropertiesMap
				.entrySet()) {
			OpenShiftDeployerProperties properties = entry.getValue();
			OpenShiftClient openShiftClient = new DefaultOpenShiftClient()
					.inNamespace(properties.getNamespace());
			ContainerFactory containerFactory = new OpenShiftContainerFactory(properties,
					new VolumeMountFactory(properties) {
					});

			OpenShiftAppDeployer openShiftAppDeployer = new OpenShiftAppDeployer(
					properties, openShiftClient, containerFactory);
			MavenOpenShiftAppDeployer mavenOpenShiftAppDeployer = new MavenOpenShiftAppDeployer(
					properties, openShiftClient, containerFactory,
					mavenResourceJarExtractor, mavenProperties, resourceHash);

			ResourceAwareOpenShiftAppDeployer appDeployer = new ResourceAwareOpenShiftAppDeployer(
					openShiftAppDeployer, mavenOpenShiftAppDeployer);
			Deployer deployer = new Deployer(entry.getKey(), "openshift", appDeployer);
			deployer.setDescription(String.format(
					"master url = [%s], namespace = [%s], api version = [%s]",
					openShiftClient.getMasterUrl(), openShiftClient.getNamespace(),
					openShiftClient.getApiVersion()));

			log.info("Adding OpenShift deployer: {}", deployer);
			deployers.add(deployer);
		}

		return new Platform("OpenShift", deployers);
	}

}
