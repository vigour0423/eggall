package com.ddl.egg.validation.testcase;

import com.google.common.collect.Lists;
import com.ddl.egg.validation.SpringTest;
import com.ddl.egg.validation.request.InnerRequest;
import com.ddl.egg.validation.request.Request;
import org.junit.Test;

/**
 * Created by mark.huang on 2016-06-08.
 */
public class TestMixValidate extends SpringTest {


    @Test(expected = IllegalArgumentException.class)
    public void testValidate2ndParam() {
        fakeSpringServiceValidated.doMultiAndMixValidate(correctRequest(), null, "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidate3rdParam() {
        fakeSpringServiceValidated.doMultiAndMixValidate(correctRequest(), 5, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateObjIntProperty() {
        Request request = correctRequest();
        request.setI(0);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateObjStrProperty() {
        Request request = correctRequest();
        request.setStr(null);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateObjCustomerProperty() {
        Request request = correctRequest();
        request.setInnerRequestList(null);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInnerObjProperty() {
        Request request = correctRequest();
        request.getInnerRequestList().get(0).setInnerInt(0);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }

    @Test(expected = ArithmeticException.class)
    public void testValidateInnerListProperty1() {
        Request request = correctRequest();
        request.setInnerRequestList2(null);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInnerListProperty2() {
        Request request = correctRequest();
        request.getInnerRequestList2().get(0).setInnerInt(null);
        fakeSpringServiceValidated.doMultiAndMixValidate(request, 5, "ab");
    }


    public Request correctRequest() {
        Request request = new Request();
        request.setI(55);
        request.setStr("ab");

        InnerRequest innerRequest1 = new InnerRequest();
        innerRequest1.setInnerInt(45);
        innerRequest1.setInnerStr("ba");

        InnerRequest innerRequest2 = new InnerRequest();
        innerRequest2.setInnerInt(45);
        innerRequest2.setInnerStr("ba");

        request.setInnerRequestList(Lists.newArrayList(innerRequest1, innerRequest2));

        InnerRequest innerRequest3 = new InnerRequest();
        innerRequest3.setInnerInt(45);
        innerRequest3.setInnerStr("ba");

        InnerRequest innerRequest4 = new InnerRequest();
        innerRequest4.setInnerInt(45);
        innerRequest4.setInnerStr("ba");
        request.setInnerRequestList2(Lists.newArrayList(innerRequest3, innerRequest4));
        return request;
    }
}
