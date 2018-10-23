package com.ddl.egg.validation.testcase;

import com.ddl.egg.validation.SpringTest;
import org.junit.Test;

/**
 * Created by mark.huang on 2016-06-08.
 */
public class TestValidateScope extends SpringTest {


    @Test(expected = IllegalArgumentException.class)
    public void callConfigCalidatedF() {
        fakeSpringServiceValidated.doF(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callConfigCalidatedS() {
        fakeSpringServiceValidated.doS(null);
    }

    @Test(expected = NullPointerException.class)
    public void callNoConfigF() {
        fakeSpringServiceNotValidated.doF(null);
    }

    @Test(expected = NullPointerException.class)
    public void callNoConfigS() {
        fakeSpringServiceNotValidated.doS(null);
    }
}
