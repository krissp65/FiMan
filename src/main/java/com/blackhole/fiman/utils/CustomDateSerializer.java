package com.blackhole.fiman.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<Date> {

	private static final long serialVersionUID = -6962744484938794793L;

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
	
	public CustomDateSerializer() {
		this(null);
	}
	
	public CustomDateSerializer(Class<Date> t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(formatter.format(value));
	}

}
