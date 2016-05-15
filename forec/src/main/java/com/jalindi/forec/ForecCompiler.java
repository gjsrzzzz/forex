package com.jalindi.forec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

@XmlRootElement
public class ForecCompiler {
	private FieldDefinition[] fields;
	private String name;
	private StringBuilder built;

	public ForecCompiler(String name, FieldDefinition[] fields) {
		this.fields = fields;
		this.name = name;

	}

	public ForecClass compile() {
		String packageName = "forec.compiled";
		built = new StringBuilder();
		built.append("package " + packageName + ";\n");
		addImport("javax.xml.bind.annotation");
		addImport("java.util");
		addImport("com.jalindi.forec.datatype");
		built.append("\n@XmlRootElement\npublic class " + name + "{\n");
		Map<String, FieldDefinition> defintions = new HashMap<>();
		for (FieldDefinition field : fields) {
			defintions.put(field.name(), field);
			append(field);
		}
		built.append("}");
		System.out.println(built.toString());
		Class<?> forecClass = compileClass(built.toString(), packageName, name);
		System.out.println(forecClass.toString());
		return new ForecClass(forecClass, defintions);
		// doCompile(built.toString());
	}

	private void addImport(String packageName) {
		built.append("\nimport " + packageName + ".*;\n");
	}

	private static Class<?> compileClass(String code, String packageName, String className) {
		try {
			JavaCompiler javac = new EclipseCompiler();

			StandardJavaFileManager sjfm = javac.getStandardFileManager(null, null, null);
			SpecialClassLoader cl = new SpecialClassLoader();
			SpecialJavaFileManager fileManager = new SpecialJavaFileManager(sjfm, cl);
			List<String> options = Collections.emptyList();
			List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(className, code));
			DiagnosticListener<JavaFileObject> dianosticListener = null;
			Iterable<String> classes = null;
			Writer out = new PrintWriter(System.err);
			JavaCompiler.CompilationTask compile = javac.getTask(out, fileManager, dianosticListener, options, classes,
					compilationUnits);
			boolean res = compile.call();
			if (res) {
				return cl.findClass(packageName + "." + className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class MemorySource extends SimpleJavaFileObject {
		private String src;

		public MemorySource(String name, String src) {
			super(URI.create("file:///" + name + ".java"), Kind.SOURCE);
			this.src = src;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return src;
		}

		@Override
		public OutputStream openOutputStream() {
			throw new IllegalStateException();
		}

		@Override
		public InputStream openInputStream() {
			return new ByteArrayInputStream(src.getBytes());
		}
	}

	private static class MemoryByteCode extends SimpleJavaFileObject {
		private ByteArrayOutputStream baos;

		public MemoryByteCode(String name) {
			super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			throw new IllegalStateException();
		}

		@Override
		public OutputStream openOutputStream() {
			baos = new ByteArrayOutputStream();
			return baos;
		}

		@Override
		public InputStream openInputStream() {
			throw new IllegalStateException();
		}

		public byte[] getBytes() {
			return baos.toByteArray();
		}
	}

	private static class SpecialClassLoader extends ClassLoader {
		private Map<String, MemoryByteCode> m = new HashMap<String, MemoryByteCode>();

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			MemoryByteCode mbc = m.get(name);
			if (mbc == null) {
				mbc = m.get(name.replace(".", "/"));
				if (mbc == null) {
					return super.findClass(name);
				}
			}
			return defineClass(name, mbc.getBytes(), 0, mbc.getBytes().length);
		}

		public void addClass(String name, MemoryByteCode mbc) {
			m.put(name, mbc);
		}
	}

	private static class SpecialJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
		private SpecialClassLoader xcl;

		public SpecialJavaFileManager(StandardJavaFileManager sjfm, SpecialClassLoader xcl) {
			super(sjfm);
			this.xcl = xcl;
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind,
				FileObject sibling) throws IOException {
			MemoryByteCode mbc = new MemoryByteCode(name);
			xcl.addClass(name, mbc);
			return mbc;
		}

		@Override
		public ClassLoader getClassLoader(Location location) {
			return xcl;
		}
	}

	private void doCompile(String code) {
		System.out.println(code);
		JavaCompiler compiler = new EclipseCompiler();// ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaFileObject file = new JavaSourceFromString(name, code);

		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
		CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

		boolean success = task.call();
		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
			System.out.println(diagnostic.getCode());
			System.out.println(diagnostic.getKind());
			System.out.println(diagnostic.getPosition());
			System.out.println(diagnostic.getStartPosition());
			System.out.println(diagnostic.getEndPosition());
			System.out.println(diagnostic.getSource());
			System.out.println(diagnostic.getMessage(null));

		}
		System.out.println("Success: " + success);

		if (success) {
			try {

				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
				Class<?> forecClass = Class.forName(name, true, classLoader);
				System.out.println("Class: " + forecClass.toString());

				// .getDeclaredMethod("main", new Class[] { String[].class
				// }).invoke(null, new Object[] { null });

			} catch (ClassNotFoundException e) {
				System.err.println("Class not found: " + e);
			} catch (MalformedURLException e) {
				System.err.println("Malformed URL: " + e);
			}
		}
	}

	private String getReadMethod(FieldDefinition field) {
		if (field.dataType().isBoolean()) {
			String method = "is" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
			return method;
		} else {
			String method = "get" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
			return method;
		}
	}

	private String getWriteMethod(FieldDefinition field) {
		if (field.dataType().isBoolean()) {
			String method = "setIs" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
			return method;
		} else {
			String method = "set" + field.name().substring(0, 1).toUpperCase() + field.name().substring(1);
			return method;
		}
	}

	private void append(FieldDefinition field) {
		String javaType = field.dataType().getJavaType();
		if (field.isList()) {
			String listType = "List<" + javaType + ">";
			built.append("\tprivate " + listType + " " + field.name() + "=new Array" + listType + "();\n");
			built.append("\tpublic " + listType + " " + getReadMethod(field) + "() { return " + field.name() + ";}\n");
			built.append("\tpublic void " + getWriteMethod(field) + "(" + listType + " " + field.name() + ")"
					+ " { this." + field.name() + "=" + field.name() + ";}\n");
		} else {
			built.append("\tprivate " + javaType + " " + field.name() + ";\n");
			built.append("\tpublic " + javaType + " " + getReadMethod(field) + "() { return " + field.name() + ";}\n");
			built.append("\tpublic void " + getWriteMethod(field) + "(" + javaType + " " + field.name() + ")"
					+ " { this." + field.name() + "=" + field.name() + ";}\n");
		}
	}

	class JavaSourceFromString extends SimpleJavaFileObject {
		final String code;

		JavaSourceFromString(String name, String code) {
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return code;
		}
	}
}
