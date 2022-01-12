/*
 * Copyright (c) 2015. DENODO Technologies.
 * http://www.denodo.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of DENODO
 * Technologies ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with DENODO.
 */

package com.denodo.vdp.demo.function.custom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.denodo.common.custom.annotations.CustomElement;
import com.denodo.common.custom.annotations.CustomElementType;
import com.denodo.common.custom.annotations.CustomExecutor;
import com.denodo.common.custom.annotations.CustomExecutorReturnType;
import com.denodo.common.custom.annotations.CustomParam;
import com.denodo.common.custom.elements.CustomArrayType;
import com.denodo.common.custom.elements.CustomArrayValue;
import com.denodo.common.custom.elements.CustomElementsUtil;
import com.denodo.common.custom.elements.CustomRecordType;
import com.denodo.common.custom.elements.CustomRecordValue;


@CustomElement(type=CustomElementType.VDPFUNCTION, name="SPLIT_SAMPLE")
public class Split_SampleVdpFunction {

    private static final String STRING_FIELD = "string";

    @CustomExecutor
    public CustomArrayValue split_sample(@CustomParam(name="regex")String regex, @CustomParam(name="value")String value) {
        if(value == null || regex == null) {
            return null;
        }
        String []result = value.split(regex);
        LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>(1);
        List<CustomRecordValue> arrayValues = new ArrayList<CustomRecordValue>(result.length);
        for (String string : result) {
            results.put(STRING_FIELD, string);
            CustomRecordValue recordValue = CustomElementsUtil.createCustomRecordValue(results);
            arrayValues.add(recordValue);
        }

        return CustomElementsUtil.createCustomArrayValue(arrayValues);
    }

    @CustomExecutorReturnType
    public CustomArrayType split_sampleReturnType(String regex, String value) {
        LinkedHashMap<String, Object> props = new LinkedHashMap<String, Object>();
        props.put(STRING_FIELD, String.class);
        CustomRecordType record = CustomElementsUtil.createCustomRecordType(props);
        CustomArrayType array = CustomElementsUtil.createCustomArrayType(record);
        return array;
    }


}
