package com.ddl.egg.validation;

import com.ddl.egg.validation.mock.EnableValidateConfigurationMock;
import com.ddl.egg.validation.mock.service.novalidate.FakeSpringServiceNotValidated;
import com.ddl.egg.validation.mock.service.validate.FakeSpringServiceValidated;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static com.ddl.egg.BaseConstants.PROFILE_TEST;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({@ContextConfiguration(classes = {TestConfig.class, EnableValidateConfigurationMock.class})})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ActiveProfiles(PROFILE_TEST)
public abstract class SpringTest {


    @Autowired
    protected FakeSpringServiceValidated fakeSpringServiceValidated;
    @Autowired
    protected FakeSpringServiceNotValidated fakeSpringServiceNotValidated;
}
