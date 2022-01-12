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

public class GlobalNames {

    public static final String PROJECT_NAME = "Samples";
    public static final String PROJECT_DESCRIPTION = "Project for samples";

    public static final String DEFAULT_LOGIN = "admin";
    public static final String DEFAULT_PHRASE = "admin";

    public static final String DATASOURCE_ARN_INDEX_NAME = "default-arn-index-ds";
    public static final Integer DATASOURCE_ARN_INDEX_PORT = 9000;

    public static final Long DATASOURCE_ITP_QUERY_TIMEOUT = 80000L;
    public static final Long DATASOURCE_ITP_CHUNK_TIMEOUT = 90000L;
    public static final Long DATASOURCE_ITP_CHUNK_SIZE = 100L;

    public static final Long DATASOURCE_VDP_QUERY_TIMEOUT = 80000L;
    public static final Long DATASOURCE_VDP_CHUNK_TIMEOUT = 90000L;
    public static final Long DATASOURCE_VDP_CHUNK_SIZE = 100L;
    public static final Boolean DATASOURCE_VDP_POOL_ENABLED = true;
    public static final Integer DATASOURCE_VDP_POOL_INIT_SIZE = 0;
    public static final Integer DATASOURCE_VDP_POOL_MAX_ACTIVE = 30;
    public static final Integer DATASOURCE_VDP_POOL_MAX_IDLE = 20;

    public static final Integer DATASOURCE_JDBC_QUERY_TIMEOUT = 80000;
    public static final Integer DATASOURCE_JDBC_CHUNK_TIMEOUT = 90000;
    public static final Integer DATASOURCE_JDBC_CHUNK_SIZE = 100;
    public static final Boolean DATASOURCE_JDBC_POOL_ENABLED = true;
    public static final Integer DATASOURCE_JDBC_POOL_INIT_SIZE = 0;
    public static final Integer DATASOURCE_JDBC_POOL_MAX_ACTIVE = 8;
    public static final Long DATASOURCE_JDBC_POOL_MAX_WAIT = -1L;
    public static final Integer DATASOURCE_JDBC_POOL_MAX_IDLE = 8;
    public static final Integer DATASOURCE_JDBC_POOL_MIN_IDLE = 0;
    public static final Integer DATASOURCE_JDBC_POOL_EXAUSTED_ACTION = 1;
    public static final Boolean DATASOURCE_JDBC_POOL_TEST_ON_BORROW = false;
    public static final Boolean DATASOURCE_JDBC_POOL_TEST_ON_RETURN = false;
    public static final Boolean DATASOURCE_JDBC_POOL_TEST_WHILE_IDLE = false;
    public static final Integer DATASOURCE_JDBC_POOL_TIME_BETWEEN_EVICTION = -1;
    public static final Integer DATASOURCE_JDBC_POOL_NUM_TEST_PER_EVICTION = 3;
    public static final Integer DATASOURCE_JDBC_POOL_MIN_EVIDECTABLE_TIME = 1800000;
    public static final Boolean DATASOURCE_JDBC_POOL_PREPARED_STATEMENTS = false;
    public static final Integer DATASOURCE_JDBC_POOL_MAX_SLEEPING_PREPARED_STATEMENTS = 8;
    public static final Integer DATASOURCE_JDBC_POOL_INITIAL_CAPACITY_PREPARED_STATEMENTS = 4;

    public static final String DEFAULT_SCHEDULER_HOST = "localhost";
    public static final Integer DEFAULT_SCHEDULER_PORT = 8000;
    
    private GlobalNames() {
        throw new AssertionError();
    }

}
