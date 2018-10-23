package com.ddl.egg.rest.client.api;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

public class EnableAPISelector extends AdviceModeImportSelector<EnableAPI> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        if (adviceMode != AdviceMode.PROXY)
            throw new IllegalStateException("@EnableAPI only support PROXY advice mode.");
        return new String[]{EnableAPIConfiguration.class.getName()};
    }
}
