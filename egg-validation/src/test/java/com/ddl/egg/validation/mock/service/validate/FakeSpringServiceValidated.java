package com.ddl.egg.validation.mock.service.validate;

import com.ddl.egg.validation.custom.v2.annotation.CollectionVerify;
import com.ddl.egg.validation.custom.v2.annotation.IntNotEmpty;
import com.ddl.egg.validation.mock.MockService;
import com.ddl.egg.validation.request.Request;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Created by mark on 2017/3/29.
 */
@MockService
public class FakeSpringServiceValidated {

    public void doF(@NotEmpty String str) {
        str.toString();
    }

    public void doS(@IntNotEmpty Integer i) {
        int ii = i;
    }

    public void doMultiBaseParam(@NotEmpty String str, @Min(50) Integer i) {
        str.toString();
        int iii = 10 / i;
    }

    public void doParamWithCustomValidate(@IntNotEmpty Integer i) {
        int iii = 10 / i;
    }

    public void doParamWithCustomValidate(@CollectionVerify(nullable = false, min = 2, max = 10) List<String> strings) {
        int iii = 11 / strings.size();
    }

    public void doMultiBaseParamSingle(@NotEmpty String str, @Min(50) Integer i) {
        str.toString();
        int iii = 10 / i;
    }

    public void doMultiAndMixValidate(@Valid Request request, @IntNotEmpty Integer i, @NotEmpty String str) {
        int iii = 11 / 0;
    }
}
