package com.ddl.egg.validation.testcase;

import com.google.common.collect.Lists;
import com.ddl.egg.validation.SpringTest;
import org.junit.Test;

import java.util.List;

/**
 * Created by mark.huang on 2016-06-08.
 */
public class TestMultiValidate extends SpringTest {


    @Test(expected = IllegalArgumentException.class)
    public void testMulti1() {
        fakeSpringServiceValidated.doMultiBaseParam("", 55);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMult2() {
        fakeSpringServiceValidated.doMultiBaseParam("a", 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMult3() {
        fakeSpringServiceValidated.doParamWithCustomValidate(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMult4() {
        List<String> strings = Lists.newArrayList("abc", null);
        fakeSpringServiceValidated.doParamWithCustomValidate(strings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiSigle1() {
        fakeSpringServiceValidated.doMultiBaseParamSingle(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiSigle2() {
        fakeSpringServiceValidated.doMultiBaseParamSingle("a", 0);
    }
}
