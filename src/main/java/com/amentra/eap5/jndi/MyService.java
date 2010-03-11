package com.amentra.eap5.jndi;

import java.io.Serializable;

@JNDI
public class MyService implements Serializable {
	private static final long serialVersionUID = -3564138583343849333L;

	void execute() {
		System.out.println("Hi from MyService");
	}
}
