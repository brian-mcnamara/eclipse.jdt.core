/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import org.eclipse.core.runtime.IPath;

/** 
 * An entry on a Java project classpath identifying one or more package fragment
 * roots. A classpath entry has a content kind (either source, 
 * <code>K_SOURCE</code>, or binary, <code>K_BINARY</code>), which is inherited
 * by each package fragment root and package fragment associated with the entry.
 * <p>
 * A classpath entry can refer to any of the following:<ul>
 * 
 *	<li>Source code in the current project. In this case, the entry identifies a
 *		root folder in the current project containing package fragments and
 *		<code>.java</code> source files. The root folder itself represents a default
 *		package, subfolders represent package fragments, and <code>.java</code> files
 *		represent compilation units. All compilation units will be compiled when
 * 		the project is built. The classpath entry must specify the
 *		absolute path to the root folder. Entries of this kind are 
 *		associated with the <code>CPE_SOURCE</code> constant.
 *      Source classpath entries can carry inclusion and exclusion patterns for
 *      selecting which <code>.java</code> source files appear as compilation
 *      units and get compiled when the project is built.
 *  </li>
 * 
 *	<li>A binary library in the current project, in another project, or in the external
 *		file system. In this case the entry identifies a JAR (or root folder) containing
 *		package fragments and <code>.class</code> files.  The classpath entry
 *		must specify the absolute path to the JAR (or root folder), and in case it refers
 *		to an external JAR, then there is no associated resource in the workbench. Entries 
 *		of this kind are associated with the <code>CPE_LIBRARY</code> constant.</li>
 * 
 *	<li>A required project. In this case the entry identifies another project in
 *		the workspace. The required project is used as a binary library when compiling
 *		(that is, the builder looks in the output location of the required project
 *		for required <code>.class</code> files when building). When performing other
 *		"development" operations - such as code assist, code resolve, type hierarchy
 *		creation, etc. - the source code of the project is referred to. Thus, development
 *		is performed against a required project's source code, and compilation is 
 *		performed against a required project's last built state.  The
 *		classpath entry must specify the absolute path to the
 *		project. Entries of this kind are  associated with the <code>CPE_PROJECT</code>
 *		constant. 
 * 		Note: referencing a required project with a classpath entry refers to the source 
 *     code or associated <code>.class</code> files located in its output location. 
 *     It will also automatically include any other libraries or projects that the required project's classpath 
 *     refers to, iff the corresponding classpath entries are tagged as being exported 
 *     (<code>IClasspathEntry#isExported</code>). 
 *    Unless exporting some classpath entries, classpaths are not chained by default - 
 *    each project must specify its own classpath in its entirety.</li>
 * 
 *  <li> A path beginning in a classpath variable defined globally to the workspace.
 *		Entries of this kind are  associated with the <code>CPE_VARIABLE</code> constant.  
 *      Classpath variables are created using <code>JavaCore#setClasspathVariable</code>,
 * 		and gets resolved, to either a project or library entry, using
 *      <code>JavaCore#getResolvedClasspathVariable</code>.
 *		It is also possible to register an automatic initializer (<code>ClasspathVariableInitializer</code>),
 * 	which will be invoked through the extension point "org.eclipse.jdt.core.classpathVariableInitializer".
 * 	After resolution, a classpath variable entry may either correspond to a project or a library entry. </li>
 * 
 *  <li> A named classpath container identified by its container path.
 *     A classpath container provides a way to indirectly reference a set of classpath entries through
 *     a classpath entry of kind <code>CPE_CONTAINER</code>. Typically, a classpath container can
 *     be used to describe a complex library composed of multiple JARs, projects or classpath variables,
 *     considering also that containers can be mapped differently on each project. Several projects can
 *     reference the same generic container path, but have each of them actually bound to a different
 *     container object.
 *     The container path is a formed by a first ID segment followed with extra segments, 
 *     which can be used as additional hints for resolving this container reference. If no container was ever 
 *     recorded for this container path onto this project (using <code>setClasspathContainer</code>, 
 * 	then a <code>ClasspathContainerInitializer</code> will be activated if any was registered for this 
 * 	container ID onto the extension point "org.eclipse.jdt.core.classpathContainerInitializer".
 * 	A classpath container entry can be resolved explicitly using <code>JavaCore#getClasspathContainer</code>
 * 	and the resulting container entries can contain any non-container entry. In particular, it may contain variable
 *     entries, which in turn needs to be resolved before being directly used. 
 * 	<br> Also note that the container resolution APIs include an IJavaProject argument, so as to allow the same
 * 	container path to be interpreted in different ways for different projects. </li>
 * </ul>
 * </p>
 * The result of <code>IJavaProject#getResolvedClasspath</code> will have all entries of type
 * <code>CPE_VARIABLE</code> and <code>CPE_CONTAINER</code> resolved to a set of 
 * <code>CPE_SOURCE</code>, <code>CPE_LIBRARY</code> or <code>CPE_PROJECT</code>
 * classpath entries.
 * <p>
 * Any classpath entry other than a source folder (kind <code>CPE_SOURCE</code>) can
 * be marked as being exported. Exported entries are automatically contributed to
 * dependent projects, along with the project's default output folder, which is
 * implicitly exported, and any auxiliary output folders specified on source
 * classpath entries. The project's output folder(s) are always listed first,
 * followed by the any exported entries.
 * <p>
 * This interface is not intended to be implemented by clients.
 * Classpath entries can be created via methods on <code>JavaCore</code>.
 * </p>
 *
 * @see JavaCore#newLibraryEntry(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
 * @see JavaCore#newProjectEntry(org.eclipse.core.runtime.IPath)
 * @see JavaCore#newSourceEntry(org.eclipse.core.runtime.IPath)
 * @see JavaCore#newVariableEntry(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
 * @see JavaCore#newContainerEntry(org.eclipse.core.runtime.IPath)
 * @see ClasspathVariableInitializer
 * @see ClasspathContainerInitializer
 */
public interface IClasspathEntry {

	/**
	 * Entry kind constant describing a classpath entry identifying a
	 * library. A library is a folder or JAR containing package
	 * fragments consisting of pre-compiled binaries.
	 */
	int CPE_LIBRARY = 1;

	/**
	 * Entry kind constant describing a classpath entry identifying a
	 * required project.
	 */
	int CPE_PROJECT = 2;

	/**
	 * Entry kind constant describing a classpath entry identifying a
	 * folder containing package fragments with source code
	 * to be compiled.
	 */
	int CPE_SOURCE = 3;

	/**
	 * Entry kind constant describing a classpath entry defined using
	 * a path that begins with a classpath variable reference.
	 */
	int CPE_VARIABLE = 4;

	/**
	 * Entry kind constant describing a classpath entry representing
	 * a name classpath container.
	 * 
	 * @since 2.0
	 */
	int CPE_CONTAINER = 5;
	
	/**
	 * Returns whether the access restrictions of the project's exported entries should be combined with these access restrictions.
	 * Returns true for container entries.
	 * Returns false otherwise.
	 * 
	 * @return whether the access restrictions of the project's exported entries should be combined with these access restrictions
	 * @since 3.1
	 * @deprecated use combineAccessRules() instead
	 */
	// TODO (jerome) remove before 3.1 M6
	boolean combineAccessRestrictions();

	/**
	 * Returns whether the access rules of the project's exported entries should be combined with this entry's access rules.
	 * Returns true for container entries.
	 * Returns false otherwise.
	 * 
	 * @return whether the access rules of the project's exported entries should be combined with this entry's access rules
	 * @since 3.1
	 */
	boolean combineAccessRules();

	/**
	 * Returns the set of patterns used to explicitly allow access to files 
	 * associated with this classpath entry. Only project, library, container 
	 * and variable entries support accessible files patterns. An empty array 
	 * is returned for source entries.
	 * <p>
	 * Each path specified is a relative path encoding a package-qualified class name
	 * (e.g., <code>java/lang/String.class</code>). Class names that match
	 * the accessible files pattern can be legally referred to by source code in the
	 * project.
	 * </p>
	 * <p>
	 * See {@link #getNonAccessibleFiles()} for a discussion of the syntax and
	 * semantics of path patterns. The absence of any accessible files patterns is
	 * semantically equivalent to the explicit accessible files pattern
	 * <code>&#42;&#42;</code>.
	 * </p>
	 * <p>
	 * Example patterns in library entries:
	 * <ul>
	 * <li>
	 * The accessible files pattern <code>src/&#42;&#42;</code> by itself includes all
	 * files under a root folder named <code>lib</code>.
	 * </li>
	 * <li>
	 * The accessible files patterns <code>lib/&#42;&#42;</code> and
	 * <code>tests/&#42;&#42;</code> makes all files under the root folders
	 * named <code>lib</code> and <code>tests</code> accessible.
	 * </li>
	 * <li>
	 * The accessible files pattern <code>lib/&#42;&#42;</code> together with the
	 * non accessible files pattern <code>lib/&#42;&#42;/Foo.class</code> makes all
	 * files under a root folder named <code>lib</code> accessible except for ones
	 * named <code>Foo.class</code>.
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @return the possibly empty list of accessible files patterns 
	 *   associated with this classpath entry, or an empty array if this kind
	 *   of classpath entry does not support accessible files patterns
	 * @since 3.1
	 * @deprecated use getAccessRules() instead
	 */
	// TODO (jerome) remove before 3.1 M6
	IPath[] getAccessibleFiles();
	/**
	 * Returns the possibly empty list of access rules for this entry.
	 * 
	 * @return the possibly empty list of access rules for this entry
	 * @since 3.1
	 */
	IAccessRule[] getAccessRules();
	/**
	 * Returns the set of patterns used to limit access to files associated with
	 * this classpath entry. Only project, library, container and variable entries
	 * support non accessible files patterns. An empty array is returned for
	 * source entries.
	 * <p>
	 * Each path specified is a relative path encoding a package-qualified class name
	 * (e.g., <code>java/lang/String.class</code>). Class names that match
	 * the non accessible files pattern should not be referred to by source code in the
	 * project (compiler will generate a warning or error).
	 * </p>
	 * <p>
	 * The pattern mechanism is similar to Ant's. Each pattern is represented as
	 * a relative path. The path segments can be regular file or folder names or simple patterns
	 * involving standard wildcard characters.
	 * </p>
	 * <p>
	 * '*' matches 0 or more characters within a segment. So
	 * <code>*.class</code> matches <code>.class</code>, <code>a.class</code>
	 * and <code>Foo.class</code>, but not <code>Foo.properties</code>
	 * (does not end with <code>.class</code>).
	 * </p>
	 * <p>
	 * '?' matches 1 character within a segment. So <code>?.class</code> 
	 * matches <code>a.class</code>, <code>A.class</code>, 
	 * but not <code>.class</code> or <code>xyz.class</code> (neither have
	 * just one character before <code>.class</code>).
	 * </p>
	 * <p>
	 * Combinations of *'s and ?'s are allowed.
	 * </p>
	 * <p>
	 * The special pattern '**' matches zero or more segments. In a library entry,
	 * a path like <code>tests/</code> that ends in a trailing separator is interpreted
	 * as <code>tests/&#42;&#42;</code>, and would match everything under
	 * the folder named <code>tests</code>.
	 * </p>
	 * <p>
	 * Example patterns in library entries:
	 * <ul>
	 * <li>
	 * <code>tests/&#42;&#42;</code> (or simply <code>tests/</code>) 
	 * matches all files under a root folder
	 * named <code>tests</code>. This makes <code>tests/Foo.class</code>
	 * and <code>tests/com/example/Foo.class</code> non accessible, but 
	 * <code>com/example/tests/Foo.class</code> (not under a root folder named
	 * <code>tests</code>) is accessible.
	 * </li>
	 * <li>
	 * <code>tests/&#42;</code> matches all files directly below a root 
	 * folder named <code>tests</code>. This makes <code>tests/Foo.class</code>
	 * and <code>tests/FooHelp.class</code> non accessible
	 * but <code>tests/com/example/Foo.class</code> (not directly under
	 * a folder named <code>tests</code>) or 
	 * <code>com/Foo.class</code> (not under a folder named <code>tests</code>)
     * are accessible.
	 * </li>
	 * <li>
	 * <code>&#42;&#42;/tests/&#42;&#42;</code> matches all files under any
	 * folder named <code>tests</code>. This makes <code>tests/Foo.class</code>,
	 * <code>com/examples/tests/Foo.class</code>, and 
	 * <code>com/examples/tests/unit/Foo.class</code> non accessible, but
	 * <code>com/example/Foo.class</code> (not under a folder named
	 * <code>tests</code>) is accessible.
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @return the possibly empty list of non accessible file patterns 
	 *   associated with this classpath entry, or an empty array if this kind
	 *   of classpath entry does not support non accessible file patterns
	 * @since 3.1
	 * @deprecated use getAccessRules() instead
	 */
	// TODO (jerome) remove before 3.1 M6
	IPath[] getNonAccessibleFiles();
	/**
	 * Returns the kind of files found in the package fragments identified by this
	 * classpath entry.
	 *
	 * @return <code>IPackageFragmentRoot.K_SOURCE</code> for files containing
	 *   source code, and <code>IPackageFragmentRoot.K_BINARY</code> for binary
	 *   class files.
	 *   There is no specified value for an entry denoting a variable (<code>CPE_VARIABLE</code>)
	 *   or a classpath container (<code>CPE_CONTAINER</code>).
	 */
	int getContentKind();

	/**
	 * Returns the kind of this classpath entry.
	 *
	 * @return one of:
	 * <ul>
	 * <li><code>CPE_SOURCE</code> - this entry describes a source root in
	 		its project
	 * <li><code>CPE_LIBRARY</code> - this entry describes a folder or JAR
	 		containing binaries
	 * <li><code>CPE_PROJECT</code> - this entry describes another project
	 *
	 * <li><code>CPE_VARIABLE</code> - this entry describes a project or library
	 *  	indirectly via a classpath variable in the first segment of the path
	 * *
	 * <li><code>CPE_CONTAINER</code> - this entry describes set of entries
	 *  	referenced indirectly via a classpath container
	 * </ul>
	 */
	int getEntryKind();

	/**
	 * Returns the set of patterns used to exclude resources or classes associated with
	 * this classpath entry.
	 * <p>
	 * For source classpath entries,
	 * exclusion patterns allow specified portions of the resource tree rooted
	 * at this source entry's path to be filtered out. If no exclusion patterns
	 * are specified, this source entry includes all relevent files. Each path
	 * specified must be a relative path, and will be interpreted relative
	 * to this source entry's path. File patterns are case-sensitive. A file
	 * matched by one or more of these patterns is excluded from the 
	 * corresponding package fragment root.
	 * Exclusion patterns have higher precedence than inclusion patterns;
	 * in other words, exclusion patterns can remove files for the ones that 
	 * are to be included, not the other way around.
	 * </p>
	 * <p>
	 * Note that there is no need to supply a pattern to exclude ".class" files
	 * because a source entry filters these out automatically.
	 * </p>
	 * <p>
	 * The pattern mechanism is similar to Ant's. Each pattern is represented as
	 * a relative path. The path segments can be regular file or folder names or simple patterns
	 * involving standard wildcard characters.
	 * </p>
	 * <p>
	 * '*' matches 0 or more characters within a segment. So
	 * <code>*.java</code> matches <code>.java</code>, <code>a.java</code>
	 * and <code>Foo.java</code>, but not <code>Foo.properties</code>
	 * (does not end with <code>.java</code>).
	 * </p>
	 * <p>
	 * '?' matches 1 character within a segment. So <code>?.java</code> 
	 * matches <code>a.java</code>, <code>A.java</code>, 
	 * but not <code>.java</code> or <code>xyz.java</code> (neither have
	 * just one character before <code>.java</code>).
	 * </p>
	 * <p>
	 * Combinations of *'s and ?'s are allowed.
	 * </p>
	 * <p>
	 * The special pattern '**' matches zero or more segments. In a source enry,
	 * a path like <code>tests/</code> that ends in a trailing separator is interpreted
	 * as <code>tests/&#42;&#42;</code>, and would match everything under
	 * the folder named <code>tests</code>.
	 * </p>
	 * <p>
	 * Example patterns in source entries:
	 * <ul>
	 * <li>
	 * <code>tests/&#42;&#42;</code> (or simply <code>tests/</code>) 
	 * matches all files under a root folder
	 * named <code>tests</code>. This includes <code>tests/Foo.java</code>
	 * and <code>tests/com/example/Foo.java</code>, but not 
	 * <code>com/example/tests/Foo.java</code> (not under a root folder named
	 * <code>tests</code>).
	 * </li>
	 * <li>
	 * <code>tests/&#42;</code> matches all files directly below a root 
	 * folder named <code>tests</code>. This includes <code>tests/Foo.java</code>
	 * and <code>tests/FooHelp.java</code>
	 * but not <code>tests/com/example/Foo.java</code> (not directly under
	 * a folder named <code>tests</code>) or 
	 * <code>com/Foo.java</code> (not under a folder named <code>tests</code>).
	 * </li>
	 * <li>
	 * <code>&#42;&#42;/tests/&#42;&#42;</code> matches all files under any
	 * folder named <code>tests</code>. This includes <code>tests/Foo.java</code>,
	 * <code>com/examples/tests/Foo.java</code>, and 
	 * <code>com/examples/tests/unit/Foo.java</code>, but not 
	 * <code>com/example/Foo.java</code> (not under a folder named
	 * <code>tests</code>).
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @return the possibly empty list of resource exclusion patterns 
	 *   associated with this classpath entry, or <code>null</code> if this kind
	 *   of classpath entry does not support exclusion patterns
	 * @since 2.1
	 */
	IPath[] getExclusionPatterns();
	
	/**
	 * Returns the extra classpath attributes for this classpath entry. Returns an empty array if this entry
	 * has no extra attributes.
	 * 
	 * @return the possibly empty list of extra classpath attributes for this classpath entry
	 * @since 3.1
	 */
	IClasspathAttribute[] getExtraAttributes();
	
	/**
	 * Returns the set of patterns used to explicitly define resources or classes
	 * to be included with this classpath entry.
	 * <p>
	 * For source classpath entries,
	 * when no inclusion patterns are specified, the source entry includes all
	 * relevent files in the resource tree rooted at this source entry's path.
	 * Specifying one or more inclusion patterns means that only the specified
	 * portions of the resource tree are to be included. Each path specified
	 * must be a relative path, and will be interpreted relative to this source
	 * entry's path. File patterns are case-sensitive. A file matched by one or
	 * more of these patterns is included in the corresponding package fragment
	 * root unless it is excluded by one or more of this entrie's exclusion
	 * patterns. Exclusion patterns have higher precedence than inclusion
	 * patterns; in other words, exclusion patterns can remove files for the
	 * ones that are to be included, not the other way around.
	 * </p>
	 * <p>
	 * See {@link #getExclusionPatterns()} for a discussion of the syntax and
	 * semantics of path patterns. The absence of any inclusion patterns is
	 * semantically equivalent to the explicit inclusion pattern
	 * <code>&#42;&#42;</code>.
	 * </p>
	 * <p>
	 * Example patterns in source entries:
	 * <ul>
	 * <li>
	 * The inclusion pattern <code>src/&#42;&#42;</code> by itself includes all
	 * files under a root folder named <code>src</code>.
	 * </li>
	 * <li>
	 * The inclusion patterns <code>src/&#42;&#42;</code> and
	 * <code>tests/&#42;&#42;</code> includes all files under the root folders
	 * named <code>src</code> and <code>tests</code>.
	 * </li>
	 * <li>
	 * The inclusion pattern <code>src/&#42;&#42;</code> together with the
	 * exclusion pattern <code>src/&#42;&#42;/Foo.java</code> includes all
	 * files under a root folder named <code>src</code> except for ones
	 * named <code>Foo.java</code>.
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @return the possibly empty list of resource inclusion patterns 
	 *   associated with this classpath entry, or <code>null</code> if this kind
	 *   of classpath entry does not support inclusion patterns
	 * @since 3.0
	 */
	IPath[] getInclusionPatterns();

	/**
	 * Returns the full path to the specific location where the builder writes 
	 * <code>.class</code> files generated for this source entry 
	 * (entry kind <code>CPE_SOURCE</code>).
	 * <p>
	 * Source entries can optionally be associated with a specific output location.
	 * If none is provided, the source entry will be implicitly associated with its project
	 * default output location (see <code>IJavaProject#getOutputLocation</code>).
	 * </p><p>
	 * NOTE: A specific output location cannot coincidate with another source/library entry.
	 * </p>
	 * 
	 * @return the full path to the specific location where the builder writes 
	 * <code>.class</code> files for this source entry, or <code>null</code>
	 * if using default output folder
	 * @since 2.1
	 */
	IPath getOutputLocation();
	
	/**
	 * Returns the path of this classpath entry.
	 *
	 * The meaning of the path of a classpath entry depends on its entry kind:<ul>
	 *	<li>Source code in the current project (<code>CPE_SOURCE</code>) -  
	 *      The path associated with this entry is the absolute path to the root folder. </li>
	 *	<li>A binary library in the current project (<code>CPE_LIBRARY</code>) - the path
	 *		associated with this entry is the absolute path to the JAR (or root folder), and 
	 *		in case it refers to an external JAR, then there is no associated resource in 
	 *		the workbench.
	 *	<li>A required project (<code>CPE_PROJECT</code>) - the path of the entry denotes the
	 *		path to the corresponding project resource.</li>
	 *  <li>A variable entry (<code>CPE_VARIABLE</code>) - the first segment of the path 
	 *      is the name of a classpath variable. If this classpath variable
	 *		is bound to the path <it>P</it>, the path of the corresponding classpath entry
	 *		is computed by appending to <it>P</it> the segments of the returned
	 *		path without the variable.</li>
	 *  <li> A container entry (<code>CPE_CONTAINER</code>) - the path of the entry
	 * 	is the name of the classpath container, which can be bound indirectly to a set of classpath 
	 * 	entries after resolution. The containerPath is a formed by a first ID segment followed with 
	 *     extra segments that can be used as additional hints for resolving this container 
	 * 	reference (also see <code>IClasspathContainer</code>).
	 * </li>
	 * </ul>
	 *
	 * @return the path of this classpath entry
	 */
	IPath getPath();

	/**
	 * Returns the path to the source archive or folder associated with this
	 * classpath entry, or <code>null</code> if this classpath entry has no
	 * source attachment.
	 * <p>
	 * Only library and variable classpath entries may have source attachments.
	 * For library classpath entries, the result path (if present) locates a source
	 * archive or folder. This archive or folder can be located in a project of the 
	 * workspace or outside thr workspace. For variable classpath entries, the 
	 * result path (if present) has an analogous form and meaning as the 
	 * variable path, namely the first segment is the name of a classpath variable.
	 * </p>
	 *
	 * @return the path to the source archive or folder, or <code>null</code> if none
	 */
	IPath getSourceAttachmentPath();

	/**
	 * Returns the path within the source archive or folder where package fragments
	 * are located. An empty path indicates that packages are located at
	 * the root of the source archive or folder. Returns a non-<code>null</code> value
	 * if and only if <code>getSourceAttachmentPath</code> returns 
	 * a non-<code>null</code> value.
	 *
	 * @return the path within the source archive or folder, or <code>null</code> if
	 *    not applicable
	 */
	IPath getSourceAttachmentRootPath();
	
	/**
	 * Returns whether this entry is exported to dependent projects.
	 * Always returns <code>false</code> for source entries (kind
	 * <code>CPE_SOURCE</code>), which cannot be exported.
	 * 
	 * @return <code>true</code> if exported, and <code>false</code> otherwise
	 * @since 2.0
	 */
	boolean isExported();
	
	/**
	 * This is a helper method, which returns the resolved classpath entry denoted 
	 * by an entry (if it is a variable entry). It is obtained by resolving the variable 
	 * reference in the first segment. Returns <node>null</code> if unable to resolve using 
	 * the following algorithm:
	 * <ul>
	 * <li> if variable segment cannot be resolved, returns <code>null</code></li>
	 * <li> finds a project, JAR or binary folder in the workspace at the resolved path location</li>
	 * <li> if none finds an external JAR file or folder outside the workspace at the resolved path location </li>
	 * <li> if none returns <code>null</code></li>
	 * </ul>
	 * <p>
	 * Variable source attachment is also resolved and recorded in the resulting classpath entry.
	 * <p>
	 * @return the resolved library or project classpath entry, or <code>null</code>
	 *   if the given path could not be resolved to a classpath entry
	 *	<p> 
	 * Note that this deprecated API doesn't handle CPE_CONTAINER entries.
	 * 
	 * @deprecated - use JavaCore.getResolvedClasspathEntry(...)
	 */
	IClasspathEntry getResolvedEntry();	
}
