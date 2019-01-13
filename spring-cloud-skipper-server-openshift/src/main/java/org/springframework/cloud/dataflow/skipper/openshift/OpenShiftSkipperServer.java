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

package org.springframework.cloud.dataflow.skipper.openshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.deployer.spi.kubernetes.KubernetesAutoConfiguration;
import org.springframework.cloud.deployer.spi.local.LocalDeployerAutoConfiguration;
import org.springframework.cloud.skipper.server.EnableSkipperServer;
import org.springframework.cloud.skipper.server.autoconfigure.CloudFoundryPlatformAutoConfiguration;
import org.springframework.cloud.skipper.server.autoconfigure.KubernetesPlatformAutoConfiguration;

/**
 * Bootstrap class for the OpenShift 3 Spring Cloud Skipper Server.
 *
 * @author Michael Wirth
 */
@SpringBootApplication(exclude = { LocalDeployerAutoConfiguration.class,
		KubernetesAutoConfiguration.class, KubernetesPlatformAutoConfiguration.class,
		CloudFoundryPlatformAutoConfiguration.class })
@EnableSkipperServer
public class OpenShiftSkipperServer {

	protected OpenShiftSkipperServer() {
	}

	public static void main(String[] args) {
		SpringApplication.run(OpenShiftSkipperServer.class, args);
	}

}
