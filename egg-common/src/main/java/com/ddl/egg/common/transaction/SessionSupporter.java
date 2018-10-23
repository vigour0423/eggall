package com.ddl.egg.common.transaction;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lincn on 2018/1/10.
 */
public class SessionSupporter {

	private static final ThreadLocal<List<SessionInternal>> threadLocal = new ThreadLocal();

	public static List<SessionInternal> get() {
		List<SessionInternal> var = threadLocal.get();
		if (var == null) {
			var = Lists.newArrayList();
			threadLocal.set(var);
		}
		return var;
	}

	public static void purge() {
		threadLocal.remove();
	}
}
