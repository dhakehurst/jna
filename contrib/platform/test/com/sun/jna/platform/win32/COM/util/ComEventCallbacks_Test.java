/* Copyright (c) 2014 Dr David H. Akehurst (itemis), All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package com.sun.jna.platform.win32.COM.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.jna.platform.win32.COM.util.RunningObjectTable_Test.Application;
import com.sun.jna.platform.win32.COM.util.annotation.ComEventCallback;
import com.sun.jna.platform.win32.COM.util.annotation.ComInterface;
import com.sun.jna.platform.win32.COM.util.annotation.ComMethod;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import com.sun.jna.platform.win32.COM.util.annotation.ComProperty;

public class ComEventCallbacks_Test {

	Factory factory;
	
	@Before
	public void before() {
		this.factory = new Factory();
	}

	@After
	public void after() {
		this.factory.getComThread().terminate(100);
	}
	
	
	@ComObject(progId="Word.Application")
	interface ComIMsWordApp extends ComIApplication {
	}
	
	@ComInterface(iid="{00020970-0000-0000-C000-000000000046}")
	interface ComIApplication extends IUnknown, IConnectionPoint {
		@ComProperty
		boolean getVisible();
		
		@ComProperty
		void setVisible(boolean value);
		
		@ComMethod
		void Quit(boolean SaveChanges, Object OriginalFormat, Boolean RouteDocument);
	}	
	
	@ComInterface(iid="{00020A01-0000-0000-C000-000000000046}")
	interface ApplicationEvents4_Event {
		@ComEventCallback
		void WindowActive();
		
		@ComEventCallback(dispid=2)
		void Quit();
	}
	
	class ApplicationEvents4_EventListener extends AbstractComEventCallbackListener implements ApplicationEvents4_Event {
		Boolean Quit_called = null;
		
		@Override
		public void WindowActive() {
		}

		@Override
		public void Quit() {
			Quit_called = true;
		}

		@Override
		public void errorReceivingCallbackEvent(String message, Exception exception) {
			
			
		}
		
	}

	@Test
	public void advise() {
		// Create word object
		ComIMsWordApp wordObj = factory.createObject(ComIMsWordApp.class);
		ComIApplication wordApp = wordObj.queryInterface(ComIApplication.class);
		wordApp.setVisible(true);
		ApplicationEvents4_EventListener listener = new ApplicationEvents4_EventListener();
		wordApp.advise(ApplicationEvents4_Event.class, listener);
		
		wordApp.Quit(false, null, null);
		
		//Wait for event to happen
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(listener.Quit_called);
		Assert.assertTrue(listener.Quit_called);
	}

	@Test
	public void unadvise() {
		// Create word object
		ComIMsWordApp wordObj = factory.createObject(ComIMsWordApp.class);
		ComIApplication wordApp = wordObj.queryInterface(ComIApplication.class);
		
		ApplicationEvents4_EventListener listener = new ApplicationEvents4_EventListener();
		IComEventCallbackCookie cookie = wordApp.advise(ApplicationEvents4_Event.class, listener);
		
		wordApp.unadvise(ApplicationEvents4_Event.class, cookie);
		listener.Quit_called=false;
		wordApp.Quit(false, null, null);
		
		//Wait for event to happen
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(listener.Quit_called);
		Assert.assertFalse(listener.Quit_called);
	}
}
