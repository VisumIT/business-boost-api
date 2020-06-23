package com.visumIT.Business.boost.functions;

import java.lang.reflect.Field;
public class PartialUpdateValidation {
	public Object updateFields(Object body, Object base) throws IllegalAccessException{
		Field[] fields = body.getClass().getDeclaredFields();
		
		for(Field field : fields) {
			field.setAccessible(true);
			if(field.get(body)==null) {
				field.set(body, field.get(base));
			}
		}
		return body;
	} 
}
