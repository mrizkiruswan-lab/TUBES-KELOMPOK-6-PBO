package com.rental.infrastructure.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {

    private final Class<?> baseType;
    private final String typeFieldName;
    private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<>();
    private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<>();

    private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName){
        this.baseType      = baseType;
        this.typeFieldName = typeFieldName;
    }
    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName){
        return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
    }
    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> subtype, String label){
        labelToSubtype.put(label, subtype);
        subtypeToLabel.put(subtype, label);
        return this;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type){
        if (!baseType.isAssignableFrom(type.getRawType())) {
            return null;
        }

        final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<>();
        final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<>();

        for (Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()){
            TypeAdapter<?> delegate = gson.getDelegateAdapter(
                    this, TypeToken.get(entry.getValue()));
            labelToDelegate.put(entry.getKey(), delegate);
            subtypeToDelegate.put(entry.getValue(), delegate);
        }

        return new TypeAdapter<R>(){
            @Override
            public void write(JsonWriter out, R value) throws IOException {
                Class<?> srcType = value.getClass();
                String label = subtypeToLabel.get(srcType);
                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) subtypeToDelegate.get(srcType);
                if (delegate == null) {
                    throw new JsonParseException("Cannot serialize " + srcType.getName());
                }
                JsonObject obj = delegate.toJsonTree(value).getAsJsonObject();
                JsonObject clone = new JsonObject();
                clone.add(typeFieldName, new JsonPrimitive(label));
                for (Map.Entry<String, JsonElement> e : obj.entrySet()){
                    clone.add(e.getKey(), e.getValue());
                }
                Streams.write(clone, out);
            }

            @Override
            public R read(JsonReader in) throws IOException{
                JsonElement jsonElement = Streams.parse(in);
                JsonElement labelElement = jsonElement.getAsJsonObject().remove(typeFieldName);
                if (labelElement == null) {
                    throw new JsonParseException("No type discriminator field '" + typeFieldName + "'");
                }
                String label = labelElement.getAsString();
                @SuppressWarnings("unchecked")
                TypeAdapter<R> delegate = (TypeAdapter<R>) labelToDelegate.get(label);
                if (delegate == null) {
                    throw new JsonParseException("Unknown type label: " + label);
                }
                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
    private static final class Streams{
        static JsonElement parse(JsonReader reader){
            return JsonParser.parseReader(reader);
        }
        static void write(JsonElement element, JsonWriter writer) throws IOException{
            new GsonBuilder().create().getAdapter(JsonElement.class).write(writer, element);
        }
    }
}
