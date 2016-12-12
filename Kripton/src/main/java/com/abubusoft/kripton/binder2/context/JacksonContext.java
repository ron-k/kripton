package com.abubusoft.kripton.binder2.context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.abubusoft.kripton.binder2.persistence.JacksonWrapperParser;
import com.abubusoft.kripton.binder2.persistence.JacksonWrapperSerializer;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

public abstract class JacksonContext extends AbstractContext implements BinderContext {

	public JsonFactory innerFactory;

	public JacksonContext() {
		innerFactory = createInnerFactory();
	}

	public abstract JsonFactory createInnerFactory();

	@Override
	public JacksonWrapperParser createParser(byte[] data) {
		return createParser(new ByteArrayInputStream(data));
	}

	@Override
	public JacksonWrapperParser createParser(File file) {
		try {
			return new JacksonWrapperParser(innerFactory.createParser(file), getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperParser createParser(InputStream in) {
		try {
			return new JacksonWrapperParser(innerFactory.createParser(in), getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperParser createParser(Reader reader) {
		try {
			return new JacksonWrapperParser(innerFactory.createParser(reader), getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperParser createParser(String content) {
		try {
			return new JacksonWrapperParser(innerFactory.createParser(content), getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperSerializer createSerializer(File file) {
		return createSerializer(file, JsonEncoding.UTF8);
	}

	@Override
	public JacksonWrapperSerializer createSerializer(File file, JsonEncoding encoding) {
		try {
			JsonGenerator generator = innerFactory.createGenerator(file, encoding);
			generator.setPrettyPrinter(new MinimalPrettyPrinter());
			return new JacksonWrapperSerializer(generator, getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperSerializer createSerializer(OutputStream out) {
		return createSerializer(out, JsonEncoding.UTF8);
	}

	@Override
	public JacksonWrapperSerializer createSerializer(OutputStream out, JsonEncoding encoding) {
		try {
			JsonGenerator generator = innerFactory.createGenerator(out, encoding);
			generator.setPrettyPrinter(new MinimalPrettyPrinter());
			return new JacksonWrapperSerializer(generator, getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

	@Override
	public JacksonWrapperSerializer createSerializer(Writer writer) {
		try {
			JsonGenerator generator = innerFactory.createGenerator(writer);
			generator.setPrettyPrinter(new MinimalPrettyPrinter());
			return new JacksonWrapperSerializer(generator, getSupportedFormat());
		} catch (IOException e) {
			e.printStackTrace();
			throw new KriptonRuntimeException(e);
		}
	}

}
