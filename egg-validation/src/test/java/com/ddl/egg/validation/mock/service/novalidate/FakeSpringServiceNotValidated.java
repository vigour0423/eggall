package com.ddl.egg.validation.mock.service.novalidate;

import com.ddl.egg.validation.mock.MockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mark on 2017/3/29.
 */
@MockService
public class FakeSpringServiceNotValidated {

    private static final Logger logger = LoggerFactory.getLogger(FakeSpringServiceNotValidated.class);

    public void doF(String str) {
        str.toString();
    }

    public void doS(Integer i) {
        int ii = i;
    }
}
