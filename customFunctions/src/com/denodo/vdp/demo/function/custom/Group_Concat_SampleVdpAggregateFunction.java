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

import com.denodo.common.custom.annotations.CustomElement;
import com.denodo.common.custom.annotations.CustomElementType;
import com.denodo.common.custom.annotations.CustomExecutor;
import com.denodo.common.custom.annotations.CustomGroup;
import com.denodo.common.custom.annotations.CustomParam;
import com.denodo.common.custom.elements.CustomGroupValue;

@CustomElement(type = CustomElementType.VDPAGGREGATEFUNCTION, name = "GROUP_CONCAT_SAMPLE")
public class Group_Concat_SampleVdpAggregateFunction {

    @CustomExecutor
    public String group_concat_sample(
            @CustomGroup(name = "field", groupType = String.class) CustomGroupValue<String>[] values) {

        return group_concat_sample(null, null, values);
    }

    @CustomExecutor
    public String group_concat_sample(@CustomParam(name = "groupseparator") String separator, @CustomGroup(
            name = "field",
            groupType = String.class) CustomGroupValue<String>[] values) {

        return group_concat_sample(separator, null, values);
    }

    @CustomExecutor
    public String group_concat_sample(@CustomParam(name = "groupSeparator") String separator, @CustomParam(
            name = "separator") String fieldSeparator,
            @CustomGroup(name = "field", groupType = String.class) CustomGroupValue<String>[] values) {

        return group_concat_sample(Boolean.TRUE, separator, fieldSeparator, values);
    }

    @CustomExecutor
    public String group_concat_sample(@CustomParam(name = "ignoreNulls") Boolean ignoreNulls, @CustomParam(
            name = "groupSeparator") String separator, @CustomParam(name = "separator") String fieldSeparator,
            @CustomGroup(name = "field", groupType = String.class) CustomGroupValue<String>[] values) {

        StringBuilder result = new StringBuilder();
        String sep = (separator != null ? separator : ",");
        String fieldSep = (fieldSeparator != null ? fieldSeparator : "");
        long tuples = Long.MAX_VALUE;

        for (CustomGroupValue<String> customGroupValue : values) {
            if (tuples == Long.MAX_VALUE) {
                tuples = customGroupValue.size();
            } else if (tuples != customGroupValue.size()) {
                return null;
            }
        }

        boolean allNulls = true;
        for (int i = 0; i < tuples; i++) {
            StringBuilder toAdd = new StringBuilder();
            boolean first = true;
            for (CustomGroupValue<String> customGroupValue : values) {
                if (customGroupValue.size() > i) {
                    String val = customGroupValue.getValue(i);
                    if (val != null) {
                        allNulls = false;
                    }
                    if (val != null || !ignoreNulls.booleanValue()) {
                        if (first) {
                            first = false;
                        } else {
                            toAdd.append(fieldSep);
                        }
                        if (val != null) {
                            toAdd.append(val);
                        }
                    } else {
                        toAdd = new StringBuilder();
                        break;
                    }
                }
            }
            result.append((result.length() > 0 && toAdd.length() > 0 ? sep : "")).append(toAdd);
        }
        if (allNulls || result.length() == 0) {
            return null;
        }
        if (tuples == 0) {
            return null;
        }
        return result.toString();
    }
}
