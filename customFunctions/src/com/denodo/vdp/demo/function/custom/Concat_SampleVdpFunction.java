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
import com.denodo.common.custom.elements.CustomElementsUtil;
import com.denodo.common.custom.elements.QueryContext;

@CustomElement(type = CustomElementType.VDPFUNCTION, name = "CONCAT_SAMPLE")
public class Concat_SampleVdpFunction {

    @CustomExecutor(syntax = "CONCAT_SAMPLE(String str_1, String str_2 ... String str_n): String")
    public String concat_sample(String... input) {

        QueryContext queryContext = CustomElementsUtil.getQueryContext();
        /*
         * The custom function has access to the user name and roles of the user that executes it. Therefore, the function
         * can return different values depending on the user that executes it.
         * 
         * In this example, the function returns NULL for the users that do not have the role "assignprivileges".
         */
        if (queryContext.getCurrentUserRoles().contains("assignprivileges")) {            

            StringBuilder result = new StringBuilder();
            if (input != null) {

                for (String str : input) {

                    if (str == null) {

                        return null;

                    } else {

                        result.append(str);
                    }
                }

            } else {

                return null;
            }

            return result.toString();

        } else {

            return null;
        }
    }

}
