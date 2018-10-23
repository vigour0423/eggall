package com.ddl.egg.validation.testcase;

import com.ddl.egg.validation.SpringTest;
import com.ddl.egg.validation.request.Request;
import com.ddl.egg.validation.util.ValidatorUtils;
import org.junit.Test;

/**
 * Created by XPS-15 on 2017/4/12.
 */
public class TestRequest extends SpringTest {

	@Test(expected = IllegalArgumentException.class)
	public void test() {
		Request request=new Request();
		ValidatorUtils.validate(request);
	}
}
