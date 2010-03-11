package com.amentra.eap5.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.dependency.spi.ControllerContext;

public class LifecycleCallback {
	private InitialContext jndiContext;

	private InitialContext getJndiContext() {
		if(null == jndiContext) {
			try {
				System.out.println("Initializing jndiContext");
				Properties env = new Properties();
				env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.LocalOnlyContextFactory");
				env.setProperty(Context.PROVIDER_URL, "org.jboss.naming:org.jnp.interfaces");
				jndiContext = new InitialContext(env);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return jndiContext;
	}
	
	public void install(ControllerContext ctx) {
		try {
	        System.out.println("Bean " + ctx.getName() + " is being installed");
	        getJndiContext().bind(ctx.getName().toString(), ctx.getTarget());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public void uninstall(ControllerContext ctx) {
        try {
        	System.out.println("Bean " + ctx.getName() + " is being uninstalled");
			getJndiContext().unbind(ctx.getName().toString());
		} catch (NamingException e) {
			e.printStackTrace();
		}
    }
}
