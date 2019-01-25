/*******************************************************************************
 * Copyright 2012 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.eco.bio7.worldwind.swt;

import gov.nasa.worldwind.WorldWindowGLDrawable;

import javax.media.opengl.GLAutoDrawable;

import org.eclipse.swt.widgets.Control;

import com.eco.bio7.worldwind.swt.WorldWindowNewtCanvasSWT;
import com.jogamp.newt.opengl.GLWindow;

/**
 * {@link WorldWindowGLDrawable} subinterface used by the
 * {@link WorldWindowNewtCanvasSWT}.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public interface WorldWindowNewtDrawableSWT extends WorldWindowGLDrawable
{
	/**
	 * @deprecated Use the {@link #initDrawable(GLWindow, Control)} function
	 *             instead.
	 */
	@Deprecated
	@Override
	void initDrawable(GLAutoDrawable glAutoDrawable);

	void initDrawable(GLWindow window, Control swtControl);
}
