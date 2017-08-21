package com.irh.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * 处理json字符串与对象之间关系的工具类
 *
 * @author zk
 * @date 2015年8月27日 下午4:53:45
 */
public final class GsonUtil {

    private GsonUtil() {

    }

    /**
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJson(obj, null, false);
    }

    /**
     * @param obj
     * @param isExportNull
     * @return
     */
    public static String toJson(Object obj, Boolean isExportNull) {
        return toJson(obj, null, false, isExportNull);
    }

    /**
     * @param obj
     * @param datePattern
     * @return
     */
    public static String toJson(Object obj, String datePattern) {
        return toJson(obj, datePattern, false);
    }

    /**
     * @param obj
     * @param datePattern
     * @param isPrettyPrinting
     * @return
     */
    public static String toJson(Object obj, String datePattern, Boolean isPrettyPrinting) {
        return toJson(obj, datePattern, isPrettyPrinting, null);
    }

    /**
     * @param obj
     * @param datePattern
     * @param isPrettyPrinting
     * @param isExportNull
     * @return
     */
    public static String toJson(Object obj, String datePattern, Boolean isPrettyPrinting, Boolean isExportNull) {
        GsonBuilder builder = new GsonBuilder();

        if (isPrettyPrinting != null && isPrettyPrinting) {
            builder.setPrettyPrinting();
        }
        if (StringUtil.isNullOrEmpty(datePattern)) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (isExportNull != null && isExportNull) {
            builder.serializeNulls();
        }
        builder.disableHtmlEscaping();
        builder.setDateFormat(datePattern);
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

    /**
     * 序列化成为字符串（空字符串转为 ""）
     *
     * @param obj            待转换的对象
     * @param datePattern    日期格式，允许空==》默认：yyyy-MM-dd HH:mm:ss
     * @param isNeedEmptyStr 是否要将null转换成为空字符
     * @return Json数据
     */
    public static String toJsonWithEmptyStr(Object obj, String datePattern, Boolean isNeedEmptyStr) {
        GsonBuilder builder = new GsonBuilder();

        if (StringUtil.isNullOrEmpty(datePattern)) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (isNeedEmptyStr != null && isNeedEmptyStr) {
            builder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>());
        }
        builder.disableHtmlEscaping();
        builder.setDateFormat(datePattern);
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

    /**
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> clazz) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(jsonStr, clazz);
    }

    /**
     * 用Type来转换Json
     * @param jsonStr Json字符串
     * @param type  类型
     * @param <T>   返回类型
     * @return 数据
     */
    public static <T> T jsonStrToObjectByType(String jsonStr, Type type) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(jsonStr, type);
    }

    /**
     * pretty json string to object,replace null to empty string
     *
     * @param jsonStr
     * @param typeToken
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToObjectWithEmptyStr(String jsonStr, TypeToken<T> typeToken) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss");
        builder.registerTypeAdapter(String.class, new StringNullAdapter());
        Gson gson = builder.create();

        return gson.fromJson(jsonStr, typeToken.getType());
    }

    /**
     * 转化Json成为Map<String, Object>
     *
     * @param json 待转化的Json
     * @return Map类型的结果
     */
    public static Map<String, Object> parse2Map(String json) {

        Type stringStringMap = new TypeToken<Map<String, Object>>() {
        }.getType();

        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, stringStringMap);

        return map;
    }

}

class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return (TypeAdapter<T>) new StringNullAdapter();
    }
}

class StringNullAdapter extends TypeAdapter<String> {
    @Override
    public String read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return "";
        }
        return reader.nextString();
    }

    @Override
    public void write(JsonWriter writer, String value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value);
    }
}
