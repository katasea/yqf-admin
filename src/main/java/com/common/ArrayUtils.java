package com.common;

import java.lang.reflect.Field;

public class ArrayUtils {

	public static Field[] addAll(Field[] fields, Field[] declaredFields) {
		Field[] result = new Field[fields.length+declaredFields.length];
		int i = 0;
		for(Field o : fields) {
			result[i] = o;
			i++;
		}
		for(Field o : declaredFields) {
			result[i] = o;
			i++;
		}
		return result;
	}

}
