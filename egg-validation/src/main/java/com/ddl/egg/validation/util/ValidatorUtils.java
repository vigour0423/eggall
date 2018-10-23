package com.ddl.egg.validation.util;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * Created by lincn on 2017/4/12.
 */
public class ValidatorUtils {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * 验证
	 * @param obj
	 * @param <T>
	 */
	public static <T> void validate(T obj) {
		Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
		if (!CollectionUtils.isEmpty(set)) {
			StringBuffer sb = new StringBuffer();
			for (ConstraintViolation<T> cv : set) {
				sb.append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append(";");
			}
			throw new IllegalArgumentException(sb.toString());
		}
	}
}
