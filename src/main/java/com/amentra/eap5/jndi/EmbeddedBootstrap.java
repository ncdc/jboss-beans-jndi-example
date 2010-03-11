package com.amentra.eap5.jndi;

import java.net.URL;

import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;

public class EmbeddedBootstrap extends BasicBootstrap {
	protected BasicXMLDeployer deployer;

	public EmbeddedBootstrap() throws Exception {
		super();
	}

	public void bootstrap() throws Throwable {
		super.bootstrap();
		deployer = new BasicXMLDeployer(getKernel());
		Runtime.getRuntime().addShutdownHook(new Shutdown());
	}

	public void deploy(URL url) {
        try {
        	deployer.deploy(url);
        } catch (Throwable t) {
        	t.printStackTrace();
        }
    }

	public void undeploy(URL url) {
        deployer.undeploy(url);
    }
	
	public void shutdown() {
		deployer.shutdown();
	}

	protected class Shutdown extends Thread {
		public void run() {
			log.info("Shutting down");
			deployer.shutdown();
		}
	}
}