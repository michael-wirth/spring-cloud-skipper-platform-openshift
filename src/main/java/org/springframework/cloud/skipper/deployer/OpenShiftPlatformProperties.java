package org.springframework.cloud.skipper.deployer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.deployer.spi.openshift.OpenShiftDeployerProperties;

/**
 * @author Donovan Muller
 */
@ConfigurationProperties("spring.cloud.skipper.server.platform.openshift")
public class OpenShiftPlatformProperties {

	private Map<String, OpenShiftDeployerProperties> accounts = new LinkedHashMap<>();

	public Map<String, OpenShiftDeployerProperties> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, OpenShiftDeployerProperties> accounts) {
		this.accounts = accounts;
	}
}
