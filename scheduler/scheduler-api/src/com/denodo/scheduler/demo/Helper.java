/*
* Copyright (c) 2007. DENODO Technologies.
* http://www.denodo.com
* All rights reserved.
*
* This software is the confidential and proprietary information of DENODO
* Technologies ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with DENODO.
*/
package com.denodo.scheduler.demo;

import java.util.Collection;

import com.denodo.configuration.CompoundParameter;
import com.denodo.configuration.Parameter;
import com.denodo.configuration.ParameterStructure;
import com.denodo.configuration.SimpleParameter;

public final class Helper {

    public static Parameter<Object> buildSimpleMonoValuedParam(String name, Object value) {

        Parameter<Object> param = new SimpleParameter<>();
        param.setName(name);
        param.addValue(value);

        return param;
    }

    public static Parameter<String> buildSimpleMultivaluedParam(String name, Collection<String> values) {

        Parameter<String> param = new SimpleParameter<>(name, values);

        return param;
    }

    public static Parameter<ParameterStructure> buildCompoundParam(String name,
            Collection<ParameterStructure> values) {

        Parameter<ParameterStructure> param = new CompoundParameter();
        param.setName(name);
        for (ParameterStructure parameter : values) {
            param.addValue(parameter);
        }

        return param;
    }

}
