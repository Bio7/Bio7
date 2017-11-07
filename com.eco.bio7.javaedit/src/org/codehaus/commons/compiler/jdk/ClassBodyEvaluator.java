/*
 * Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2001-2010 Arno Unkrig. All rights reserved.
 * Copyright (c) 2015-2016 TIBCO Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *       following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *       following disclaimer in the documentation and/or other materials provided with the distribution.
 *    3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.codehaus.commons.compiler.jdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.codehaus.commons.io.MultiReader;
import org.codehaus.commons.nullanalysis.Nullable;

import com.eco.bio7.compile.CompilerMessages;

/**
 * To set up a {@link ClassBodyEvaluator} object, proceed as described for
 * {@link IClassBodyEvaluator}. Alternatively, a number of "convenience
 * constructors" exist that execute the described steps instantly.
 * <p>
 * <b>Notice that this implementation of {@link IClassBodyEvaluator} is prone to
 * "Java injection", i.e. an application could get more than one class body
 * compiled by passing a bogus input document.</b>
 * </p>
 * <p>
 * <b>Also notice that the parsing of leading IMPORT declarations is heuristic
 * and has certain limitations; see
 * {@link #parseImportDeclarations(Reader)}.</b>
 * </p>
 *
 * @see IClassBodyEvaluator
 */
public class ClassBodyEvaluator extends SimpleCompiler implements IClassBodyEvaluator {

	@Nullable
	private String[] optionalDefaultImports;
	private String className = IClassBodyEvaluator.DEFAULT_CLASS_NAME;
	@Nullable
	private Class<?> optionalExtendedType;
	private Class<?>[] implementedTypes = new Class[0];

	/*
	 * {@code null} means "not yet cooked".
	 */
	@Nullable
	private Class<?> result;

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void setDefaultImports(@Nullable String[] optionalDefaultImports) {
		this.optionalDefaultImports = optionalDefaultImports;
	}

	@Override
	public void setExtendedClass(@Nullable Class<?> optionalExtendedType) {
		this.optionalExtendedType = optionalExtendedType;
	}

	/**
	 * @deprecated Use {@link #setExtendedClass(Class)} instead
	 */
	@Deprecated
	@Override
	public void setExtendedType(@Nullable Class<?> optionalExtendedClass) {
		this.setExtendedClass(optionalExtendedClass);
	}

	@Override
	public void setImplementedInterfaces(Class<?>[] implementedTypes) {
		this.implementedTypes = implementedTypes;
	}

	/**
	 * @deprecated Use {@link #setImplementedInterfaces(Class[])} instead
	 */
	@Deprecated
	@Override
	public void setImplementedTypes(Class<?>[] implementedInterfaces) {
		this.setImplementedInterfaces(implementedInterfaces);
	}

	@Override
	public void cook(@Nullable String optionalFileName, Reader r) throws CompileException, IOException {
		if (!r.markSupported())
			r = new BufferedReader(r);
		this.cook(optionalFileName, ClassBodyEvaluator.parseImportDeclarations(r), r);
	}

	public String cookSource(String optionalFileName, Reader r) throws CompileException, IOException {
		if (!r.markSupported())
			r = new BufferedReader(r);

		String co = this.cookSource(optionalFileName, ClassBodyEvaluator.parseImportDeclarations(r), r);
		return co;
	}

	/**
	 * @param imports
	 *            E.g. "java.io.*" or "static java.util.Arrays.asList"
	 * @param r
	 *            The class body to cook, without leading IMPORT declarations
	 */
	protected void cook(String optionalFileName, String[] imports, Reader r) throws CompileException, IOException {

		// Wrap the class body in a compilation unit.
		{
			StringWriter sw1 = new StringWriter();
			{
				PrintWriter pw = new PrintWriter(sw1);

				// Break the class name up into package name and simple class name.
				String packageName; // null means default package.
				String simpleClassName;
				{
					int idx = this.className.lastIndexOf('.');
					if (idx == -1) {
						packageName = "";
						simpleClassName = this.className;
					} else {
						packageName = this.className.substring(0, idx);
						simpleClassName = this.className.substring(idx + 1);
					}
				}

				// Print PACKAGE directive.
				if (!packageName.isEmpty()) {
					pw.print("package ");
					pw.print(packageName);
					pw.print(";");
				}

				// Print default imports.
				/*
				 * if (this.optionalDefaultImports != null) { for (String defaultImport :
				 * this.optionalDefaultImports) {
				 * 
				 * pw.print("import "); pw.print(defaultImport); pw.print(";");
				 * 
				 * } }
				 */
				// Print the class declaration with the default Bio7 imports!.
				pw.print(CompilerMessages.getString("Import.bio7"));

				// Print imports as declared in the document.
				if (!r.markSupported())
					r = new BufferedReader(r);
				for (int i = 0; i < imports.length; i++) {
					pw.print("import ");
					pw.print(imports[i]);

					if (i < imports.length - 1) {

						pw.println(";");
					} else {
						// leave out the last linebreak!
						pw.print(";");

					}

				}
				/*
				 * for (String imporT : imports) { pw.print("import "); pw.print(imporT);
				 * pw.println(";"); }
				 */

				pw.print("public class ");
				pw.print(simpleClassName);
				if (this.optionalExtendedType != null) {
					pw.print(" extends ");
					pw.print(this.optionalExtendedType.getCanonicalName());
				}
				if (this.implementedTypes.length > 0) {
					pw.print(" implements ");
					pw.print(this.implementedTypes[0].getName());
					for (int i = 1; i < this.implementedTypes.length; ++i) {
						pw.print(", ");
						pw.print(this.implementedTypes[i].getName());
					}
				}
				pw.print("{");
				pw.close();
			}

			StringWriter sw2 = new StringWriter();
			{
				PrintWriter pw = new PrintWriter(sw2);
				pw.print("}");
				pw.close();
			}

			// System.out.print(readString(r));
			r = new MultiReader(
					new Reader[] { new StringReader(sw1.toString()), r, new StringReader(sw2.toString()), });

		}

		/**
		 * Compile the generated compilation unit.
		 */
		super.cook("Model", r);

		try {

			// Load the "main" class through the ClassLoader that was created by
			// "SimpleCompiler.cook()". More classes (e.g. member types will be loaded
			// automatically by the JVM.
			this.result = this.getClassLoader().loadClass(this.className);
		} catch (ClassNotFoundException cnfe) {
			throw new IOException(cnfe);
		}
	}

	/* Simply get the source! */
	private String cookSource(String optionalFileName, String[] imports, Reader r)
			throws CompileException, IOException {

		// Wrap the class body in a compilation unit.
		{
			StringWriter sw1 = new StringWriter();
			{
				PrintWriter pw = new PrintWriter(sw1);

				// Break the class name up into package name and simple class name.
				String packageName; // null means default package.
				String simpleClassName;
				{
					int idx = this.className.lastIndexOf('.');
					if (idx == -1) {
						packageName = "";
						simpleClassName = this.className;
					} else {
						packageName = this.className.substring(0, idx);
						simpleClassName = this.className.substring(idx + 1);
					}
				}

				// Print PACKAGE directive.
				if (!packageName.isEmpty()) {
					pw.print("package ");
					pw.print(packageName);
					pw.print(";");
				}

				// Print the class declaration with the default Bio7 imports!.
				pw.print(CompilerMessages.getString("Import.bio7"));

				// Print imports as declared in the document.
				if (!r.markSupported())
					r = new BufferedReader(r);
				for (int i = 0; i < imports.length; i++) {
					pw.print("import ");
					pw.print(imports[i]);

					if (i < imports.length - 1) {

						pw.println(";");
					} else {
						// leave out the last linebreak!
						pw.print(";");

					}

				}

				pw.print("public class ");
				pw.print(simpleClassName);
				if (this.optionalExtendedType != null) {
					pw.print(" extends ");
					pw.print(this.optionalExtendedType.getCanonicalName());
				}
				if (this.implementedTypes.length > 0) {
					pw.print(" implements ");
					pw.print(this.implementedTypes[0].getName());
					for (int i = 1; i < this.implementedTypes.length; ++i) {
						pw.print(", ");
						pw.print(this.implementedTypes[i].getName());
					}
				}
				pw.print("{");
				pw.close();
			}

			StringWriter sw2 = new StringWriter();
			{
				PrintWriter pw = new PrintWriter(sw2);
				pw.print("}");
				pw.close();
			}

			// System.out.print(readString(r));
			r = new MultiReader(
					new Reader[] { new StringReader(sw1.toString()), r, new StringReader(sw2.toString()), });

		}

		return readString(r);
	}

	/**
	 * @return The {@link Class} created by the preceding call to
	 *         {@link #cook(Reader)}
	 */
	public Class<?> getClazz() {
		return this.result;
	}

	/**
	 * Heuristically parse IMPORT declarations at the beginning of the character
	 * stream produced by the given {@link Reader}. After this method returns, all
	 * characters up to and including that last IMPORT declaration have been read
	 * from the {@link Reader}.
	 * <p>
	 * This method does not handle comments and string literals correctly, i.e. if a
	 * pattern that looks like an IMPORT declaration appears within a comment or a
	 * string literal, it will be taken as an IMPORT declaration.
	 *
	 * @param r
	 *            A {@link Reader} that supports MARK, e.g. a {@link BufferedReader}
	 * @return The parsed imports, e.g. {@code { "java.util.*", "static
	 *         java.util.Map.Entry" }}
	 */
	public static String[] parseImportDeclarations(Reader r) throws IOException {
		final CharBuffer cb = CharBuffer.allocate(10000);
		r.mark(cb.limit());
		r.read(cb);
		cb.rewind();

		List<String> imports = new ArrayList<String>();
		int afterLastImport = 0;
		for (Matcher matcher = IMPORT_STATEMENT_PATTERN.matcher(cb); matcher.find();) {
			imports.add(matcher.group(1));
			afterLastImport = matcher.end();
		}
		r.reset();
		r.skip(afterLastImport);
		return imports.toArray(new String[imports.size()]);
	}

	private static final Pattern IMPORT_STATEMENT_PATTERN = Pattern.compile("\\bimport\\s+" + "(" + "(?:static\\s+)?"
			+ "[\\p{javaLowerCase}\\p{javaUpperCase}_\\$][\\p{javaLowerCase}\\p{javaUpperCase}\\d_\\$]*"
			+ "(?:\\.[\\p{javaLowerCase}\\p{javaUpperCase}_\\$][\\p{javaLowerCase}\\p{javaUpperCase}\\d_\\$]*)*"
			+ "(?:\\.\\*)?" + ");");

	@Override
	public Object createInstance(Reader reader) throws CompileException, IOException {

		this.cook(reader);
		try {
			return this.getClazz().newInstance();
		} catch (InstantiationException ie) {
			CompileException ce = new CompileException(
					("Class is abstract, an interface, an array class, a primitive type, or void; "
							+ "or has no zero-parameter constructor"),
					null);
			ce.initCause(ie);
			throw ce;
		} catch (IllegalAccessException iae) {
			CompileException ce = new CompileException("The class or its zero-parameter constructor is not accessible",
					null);
			ce.initCause(iae);
			throw ce;
		}
	}

}
