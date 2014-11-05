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

/**
 * Java friendly version of the IUnknown interface.
 * 
 *
 */
public interface IUnknown {
	/**
	 * Returns a proxy object for the given interface. Assuming that the
	 * interface is annotated with a ComInterface annotation that provides a
	 * valid iid.
	 * 
	 */
	<T> T queryInterface(Class<T> comInterface);
}
