/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.core.util;

/**
 * The class represents an entry in the exception table of a ICodeAttribute as 
 * specified in the JVM specifications.
 */
public interface IExceptionTableEntry {

	/**
	 * Answer back the start pc of this entry.
	 * 
	 * @return the start pc of this entry
	 */
	int getStartPC();

	/**
	 * Answer back the end pc of this entry.
	 * 
	 * @return the end pc of this entry
	 */
	int getEndPC();

	/**
	 * Answer back the handler pc of this entry.
	 * 
	 * @return the handler pc of this entry
	 */
	int getHandlerPC();

	/**
	 * Answer back the catch type index in the constant pool.
	 * 
	 * @return the catch type index in the constant pool
	 */
	int getCatchTypeIndex();

	/**
	 * Answer back the catch type name, null if getCatchTypeIndex() returns 0.
	 * This is the case for any exception handler.
	 * 
	 * @return the catch type name, null if getCatchTypeIndex() returns 0.
	 * This is the case for any exception handler
	 */
	char[] getCatchType();
}
