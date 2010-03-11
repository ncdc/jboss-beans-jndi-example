package com.amentra.eap5.jndi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JNDITest {
	private EmbeddedBootstrap bootstrap;
	private Kernel kernel;
	private KernelController controller;
	private InitialContext jndiContext;

	@Before
	public void setup() throws Exception {
		bootstrap = new EmbeddedBootstrap();
		bootstrap.run();

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource("beans.xml");
		bootstrap.deploy(url);

		kernel = bootstrap.getKernel();
		controller = kernel.getController();
		
		Properties env = new Properties();
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.LocalOnlyContextFactory");
        env.setProperty(Context.PROVIDER_URL, "org.jboss.naming:org.jnp.interfaces");
        jndiContext = new InitialContext(env);
	}
	
	@After
	public void teardown() throws Exception {
		bootstrap.shutdown();
	}
	
	@Test
	public void lookupViaMC() throws Exception {
		ControllerContext context = controller.getInstalledContext("MyService");
		assertThat(context, is(notNullValue()));
		MyService myService = (MyService) context.getTarget();
		myService.execute();
	}
	
	@Test
	public void lookupViaJNDI() throws Exception {
        MyService s = (MyService) jndiContext.lookup("MyService");
        s.execute();
	}
	
	@Test(expected=NameNotFoundException.class)
	public void ensureUnbindingUponUninstallation() throws Exception {
		assertThat(jndiContext.lookup("MyService"), is(notNullValue()));
		bootstrap.shutdown();
		jndiContext.lookup("MyService");
	}
}
