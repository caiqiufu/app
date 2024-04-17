package com.slipper.service.aspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
		System.out.println(gson.toJson(String.class));
	}

	@Override
	public Class<?> deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		try {
			String str = json.getAsString();
			return Class.forName(str);
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Class<?> src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getName());
	}
}
