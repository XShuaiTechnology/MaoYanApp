package com.bench.utils;

import java.util.List;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * 处理Json实用类
 * <ul>
 * <li><b>可以把实体类转换成json串</b></li>
 * <li><b>可以把json串转化为相应的实体类</b></li>
 * <li><b>可以处理各种集合类,转化成相应的json格式</b></li>
 * <li><b>可以把对应的json格式转化成相应集合类</b></li>
 * </ul>
 * @since 2011-10-12
 * @author fengge
 */
public final class JsonUtils {
	public static final String TAG = "JsonUtils";
	/**
	 * 内置json对象
	 */
	private JsonObject json = new JsonObject();
	/** 针对没有Expose注解的,则在转换的时候不考虑**/
	private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	/**
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public synchronized JsonUtils put(String key, String value) {
		json.addProperty(key, value);
		return this;
	}

	public synchronized JsonUtils put(String key, Character value) {
		json.addProperty(key, value);
		return this;
	}

	public synchronized JsonUtils put(String key, Number value) {
		json.addProperty(key, value);
		return this;
	}

	public synchronized JsonUtils put(String key, Boolean value) {
		json.addProperty(key, value);
		return this;
	}

	public synchronized JsonUtils put(String key, JsonElement value) {
		json.add(key, value);
		return this;
	}

	/**
	 * 把jsonObject对象转换成字符串，并返回
	 * 
	 * @return
	 */
	public String toJsonString() {
		return json.toString();
	}

	/**
	* @Description: TODO(json 转对象)
	* @param   @param json
	* @param   @param clazz
	* @param   @return    设定文件
	* @return T    返回类型
	*/
	public static <T> T json2Object(String json, Class<T> clazz) {
		T obj = null;
		try {
			obj = gson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			throw e;
		}
		return obj;
	}

	/**
	* @Description: TODO(json 转对象)
	* @param   @param json
	* @param   @param clazz
	* @param   @return    设定文件
	* @return T    返回类型
	*/
	public static String toJsonWithExpose(Object obj) {
		String jsonStr = "";
		try {
			jsonStr = gson.toJson(obj);
		} catch (JsonSyntaxException e) {
			throw e;
		}
		return jsonStr;
	}

	/**
	* @Description: TODO(json to list)
	* @param   @param jsonStr
	* @param   @param type
	* @param   @return    设定文件
	* @return List<?>    返回类型
	*/
	public static <T> List<T> json2List(String json, java.lang.reflect.Type type) {
		List<T> objList = null;
		try {
			objList = gson.fromJson(json, type);
		} catch (JsonSyntaxException e) {
			throw e;
		}
		return objList;

	}

	/**
	 * 
	* @Title: jsonToMap
	* @Description: 将json格式转换成map对象
	* @param @param jsonStr
	* @param @param type
	* @param @return    
	* @return Map<K,V>    
	* @throws
	 */
	public static <K, V> Map<K, V> jsonToMap(String jsonStr, java.lang.reflect.Type type) {
		Map<K, V> objMap = null;
		try {
			objMap = gson.fromJson(jsonStr, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objMap;
	}

	/**
	 * 将json格式转换成map对象
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static <K, V> Map<K, V> jsonToMap(String json) {
		Map<K, V> objMap = null;
		try {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
			}.getType();
			objMap = gson.fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objMap;
	}

	/**
	 * 返回Json串,<b>默认包含实体类的所有值不为null的属性</b>
	 * 
	 * @param entity
	 * @return
	 */
	public static String toJson(Object entity) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.toJson(entity);
	}

	/**
	 * 返回Json串,<b>默认包含实体类的所有值不为null的属性</b>
	 * 
	 * @param entity
	 *            要传入的实体类对象
	 * @param clazzes
	 *            不需要转化的实体类
	 * @return json串
	 * 
	 */
	public static String toJson(Object entity, final Class<?>... clazzes) {
		GsonBuilder builder = new GsonBuilder();
		// 设置除外的属性名
		builder.setExclusionStrategies(new ExclusionStrategy() {
			/*
			 * 设置要过滤的类
			 */
			public boolean shouldSkipClass(Class<?> clazz) {
				boolean flag = false;
				// 如果返回true就表示此类被忽略
				if (clazzes != null) {
					for (Class<?> c : clazzes) {
						if (clazz == c) {
							flag = true;
						}
						break;
					}
				}
				return flag;
			}

			/**
			 * 跳过的字段
			 */
			public boolean shouldSkipField(FieldAttributes attribute) {
				return false;
			}

		});
		Gson gson = builder.create();
		return gson.toJson(entity);
	}

	/**
	 * 返回Json串,<b>默认包含实体类的所有值不为null的属性</b>
	 * 
	 * @param entity
	 *            要传入的实体类
	 * @param excludeNames
	 *            不包括的字段
	 * @param clazzes
	 *            不需要转化的实体类
	 * @return
	 */
	public static String toJson(final Object entity, final List<String> excludeNames, final Class<?>... clazzes) {
		GsonBuilder builder = new GsonBuilder();
		// 设置除外的属性名
		builder.setExclusionStrategies(new ExclusionStrategy() {
			/*
			 * 设置要过滤的类
			 */
			public boolean shouldSkipClass(Class<?> clazz) {
				boolean flag = false;
				// 如果返回true就表示此类被忽略
				if (clazzes != null) {
					for (Class<?> c : clazzes) {
						if (clazz == c) {
							flag = true;
						}
						break;
					}
				}
				return flag;
			}

			/**
			 * 跳过的字段
			 */
			public boolean shouldSkipField(FieldAttributes attribute) {
				boolean flag = false;
				if (excludeNames != null) {
					flag = excludeNames.contains(attribute.getName());
				}
				return flag;
			}

		});
		Gson gson = builder.create();
		return gson.toJson(entity);
	}

	/**
	 * 返回Json串,<b>默认包含实体类的所有属性，包括值为null的属性</b>
	 * 
	 * @return json串
	 */
	public static String toJsonIncludeNull(Object entity) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(entity);
	}

	/**
	 * 返回Json串,<b>默认包含实体类的所有属性，包括值为null的属性</b>
	 * 
	 * @param clazzes
	 *            不需要转化的实体类
	 * @return json串
	 */
	public static String toJsonIncludeNull(Object entity, final Class<?>... clazzes) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		// 设置除外的属性名
		builder.setExclusionStrategies(new ExclusionStrategy() {
			/*
			 * 设置要过滤的类
			 */
			public boolean shouldSkipClass(Class<?> clazz) {
				boolean flag = false;
				// 如果返回true就表示此类被忽略
				if (clazzes != null) {
					for (Class<?> c : clazzes) {
						if (clazz == c) {
							flag = true;
						}
						break;
					}
				}
				return flag;
			}

			/**
			 * 跳过的字段
			 */
			public boolean shouldSkipField(FieldAttributes attribute) {
				return false;
			}

		});
		Gson gson = builder.create();
		return gson.toJson(entity);
	}

	/**
	 * 返回Json串,<b>默认包含实体类的为null的属性</b>
	 * 
	 * @param entity
	 *            要传入的实体类
	 * @param excludeNames
	 *            不包括的字段
	 * @return
	 */
	public static String toJsonIncludeNull(final Object entity, final List<String> excludeNames,
			final Class<?>... clazzes) {
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
		// 设置除外的属性名
		builder.setExclusionStrategies(new ExclusionStrategy() {
			/*
			 * 设置要过滤的类
			 */
			public boolean shouldSkipClass(Class<?> clazz) {
				boolean flag = false;
				// 如果返回true就表示此类被忽略
				if (clazzes != null) {
					for (Class<?> c : clazzes) {
						if (clazz == c) {
							flag = true;
						}
						break;
					}
				}
				return flag;
			}

			/**
			 * 跳过的字段
			 */
			public boolean shouldSkipField(FieldAttributes attribute) {
				boolean flag = false;
				if (excludeNames != null) {
					flag = excludeNames.contains(attribute.getName());
				}
				return flag;
			}

		});
		Gson gson = builder.create();
		return gson.toJson(entity);

	}

	/**
	 * 根据传入的json串，构造实体类对象
	 * 
	 * @param <T>
	 * @param json
	 *            传入的json字符串
	 * @param clazz
	 *            传入的实体类类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson.fromJson(json, clazz);
	}

	/**
	 * 返回Gson对象
	 * @return
	 */
	public static Gson getGson() {
		return new GsonBuilder().create();
	}

	/**
	 * 把字符串转化成相应的JsonElement对象
	 * 
	 * @param jsonData
	 * @return
	 */
	public static JsonElement parse(String jsonData) {
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(jsonData);
	}

	public static void main(String[] args) {
		JsonArray array = new JsonArray();
		String str = "[1,2,3,4]";
		JsonElement je = parse(str);
		array.add(je);
		System.out.println(je.toString());

	}

}
