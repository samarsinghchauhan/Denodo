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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.denodo.configuration.CompoundParameter;
import com.denodo.configuration.Configuration;
import com.denodo.configuration.Parameter;
import com.denodo.configuration.ParameterNotFoundException;
import com.denodo.configuration.ParameterStructure;
import com.denodo.configuration.metadata.MetaConfiguration;
import com.denodo.scheduler.client.SchedulerManager;
import com.denodo.scheduler.client.SchedulerManagerFactory;
import com.denodo.scheduler.client.commons.Context;
import com.denodo.scheduler.client.commons.ContextBuilderFactory;
import com.denodo.scheduler.client.commons.conventions.TypesConventions;
import com.denodo.scheduler.client.commons.exceptions.JobLimitExceededException;
import com.denodo.scheduler.client.job.JobData;
import com.denodo.scheduler.client.job.JobData.State;
import com.denodo.scheduler.client.project.Item;
import com.denodo.util.exceptions.DuplicateInstanceException;
import com.denodo.util.exceptions.InstanceNotFoundException;
import com.denodo.util.exceptions.InternalErrorException;

public class SchedulerJobsConfigurationSample {

    private static final String[] descriptions = {
        "enables/disables the given job:",
        "starts the given job:",
        "stops the given job:",
        "finds job names:",
        "finds the given job:",
        "creates new job:",
        "removes the given job:",
        "updates job description:",
        "updates job scheduling configuration:",
        "creates and configures a new data source:"
    };

    private static final String[] options = {
        "-enable", "-start", "-stop", "-findNames", "-findJob", "-addjob",
        "-removejob", "-udesc", "-usched", "-adddatasource"};

    private static final String[] params = {
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort> description type",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "jobName -h <SchedulerHost> -p <SchedulerPort> description",
        "jobName -h <SchedulerHost> -p <SchedulerPort>",
        "dataSourceName -h <SchedulerHost> -p <SchedulerPort> dataSourceType"

    };

    private static int option;
    private static String host = null;
    private static int port = -1;

    private static Integer projectId = null;
    private static Map<String, Integer> dataSources =
        new HashMap<>();

    /**
     * Prints the usage mode of this example to the standard output.
     */
    private static void usage() {

        System.out.println("Usage: ");
        System.out.println("- to view this help information: ");
        System.out.println("\ttest_schedulerclient.{bat|sh} -help");

        for (int i = 0; i < options.length; i++) {
            System.out.println("- " + descriptions[i]);
            System.out.println("\ttest_schedulerclient.{bat|sh} " 
                    + options[i] + " " + params[i]);
        }
    }

    /**
     * Checks if a value is contained in a set of values.
     * @param value value.
     * @param values set of values.
     * @return if the value is contained returns the index,
     *         else returns <code>-1</code>.
     */
    private static int checkValue(String value, String[] values) {

        int result = -1;
        for (int i = 0; i < values.length; i++) {
            if (value.compareToIgnoreCase(values[i]) == 0) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Prints to the standard output that the option is unknown and the
     * usage mode.
     * @param value the unknow option.
     */
    private static void showUnknownOption(String value) {

        System.out.println("Unknown option '" + value + "'");
        usage();
    }

    /**
     * Prints to the standard output that the option is duplicated and
     * the usage mode.
     * @param value the duplicate option.
     */
    private static void showDuplicateOption(String value) {

        System.out.println("Duplicate value for the " +
                value + " parameter");
        usage();
    }

    /**
     * Prints to the standard output that the value is illegal for an option
     * and the usage mode.
     * @param name the value.
     * @param value the option.
     */
    private static void showIllegalValue(String name, String value) {

        System.out.println("Illegal value for " + name + ": " + value);
        usage();
    }

    /**
     * Process the options.
     * @param args the options.
     * @param index the index of the option.
     * @return <code>0</code> if it is correct,
     *         <code>-1</code> if it is not correct.
     */
    private static int processOptions(String[] args, int index) {

        if (args.length == index + 1) {
            showIllegalValue(args[index], null);
        }

        String optionName = args[index].toLowerCase();
        if (optionName.equals("-h")) {

            if (host == null) {
                if (args[index + 1].equals("-p")) {
                    showIllegalValue(args[index], args[index + 1]);
                    return -1;
                }
                host = args[index + 1];
                return 0;
            }
            showDuplicateOption(args[index]);
            return -1;

        } else if (optionName.equals("-p")) {

            if (port == -1) {
                if (args[index + 1].equals("-h")) {
                    showIllegalValue(args[index], args[index + 1]);
                    return -1;
                }
                port = Integer.parseInt(args[index + 1]);
                return 0;
            }
            showDuplicateOption(args[index]);
            return -1;

        } else {
            showUnknownOption(args[index]);
            return -1;
        }
    }

    /***    DATA SOURCES     ***/

    /**
     * Creates a new data source
     * @param dataSourceName
     * @param dataSourceType
     * @param schedulerManager
     * @throws ConnectException
     * @throws DuplicateInstanceException
     * @throws JobLimitExceededException
     * @throws InternalErrorException
     * @throws InstanceNotFoundException
     */
    public static void addDataSource(String dataSourceName, SchedulerManager schedulerManager,
            String dataSourceType) throws ConnectException,
            DuplicateInstanceException, InternalErrorException,
            InstanceNotFoundException {

        Configuration config = buildDataSourceConfiguration(dataSourceName,
                dataSourceType, schedulerManager);

        Configuration createdConfig =
            schedulerManager.addDataSource(projectId, config);

        dataSources.put(dataSourceType, createdConfig.getId());


        findDataSource(createdConfig, schedulerManager);

    }

    private static Configuration buildDataSourceConfiguration(
            String dataSourceName, String dataSourceType, SchedulerManager schedulerManager)
            throws ConnectException, InstanceNotFoundException, InternalErrorException {

        Configuration configuration = null;

        if (dataSourceType.compareTo(TypesConventions.ARN_INDEX_SUBTYPE) == 0) {
            configuration = buildARNIndexDS(dataSourceName, schedulerManager);

        } else if (dataSourceType.compareTo(TypesConventions.JDBC_SUBTYPE) == 0) {
            configuration = buildJDBCDS(dataSourceName, schedulerManager);

        } else if (dataSourceType.compareTo(TypesConventions.VDP_SUBTYPE) == 0) {
            configuration = buildVDPDS(dataSourceName, schedulerManager);

        } else if (dataSourceType.compareTo(TypesConventions.ITP_SUBTYPE) == 0) {
            configuration = buildITPDS(dataSourceName, schedulerManager);

        }

        return configuration;
    }

    private static Configuration buildITPDS(String dataSourceName,
            SchedulerManager schedulerManager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        MetaConfiguration metaConfig =
            schedulerManager.getMetaConfiguration("dataSource", "ITP");

        ParameterStructure defaultConfig =
            metaConfig.getDefaultConfiguration();

        Configuration dsITP = new Configuration();
        dsITP.setDraft(false);
        dsITP.setType("dataSource");
        dsITP.setSubType("ITP");

        defaultConfig.put(Helper.buildSimpleMonoValuedParam("name",
                dataSourceName));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("queryTimeout",
                GlobalNames.DATASOURCE_ITP_QUERY_TIMEOUT));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("chunkTimeout",
                GlobalNames.DATASOURCE_ITP_CHUNK_TIMEOUT));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("chunkSize",
                GlobalNames.DATASOURCE_ITP_CHUNK_SIZE));

        dsITP.setParameters(defaultConfig);

        return dsITP;

    }

    private static Configuration buildVDPDS(String dataSourceName,
            SchedulerManager schedulerManager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        MetaConfiguration metaConfig =
            schedulerManager.getMetaConfiguration("dataSource", "VDP");

        ParameterStructure defaultConfig =
            metaConfig.getDefaultConfiguration();

        Configuration dsVDP = new Configuration();
        dsVDP.setDraft(false);
        dsVDP.setType("dataSource");
        dsVDP.setSubType("VDP");

        defaultConfig.put(Helper.buildSimpleMonoValuedParam("name",
                dataSourceName));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("connectionURI",
                "//localhost:9999/admin"));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("queryTimeout",
                GlobalNames.DATASOURCE_VDP_QUERY_TIMEOUT));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("chunkTimeout",
                GlobalNames.DATASOURCE_VDP_CHUNK_TIMEOUT));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("chunkSize",
                GlobalNames.DATASOURCE_VDP_CHUNK_SIZE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolEnabled",
                GlobalNames.DATASOURCE_VDP_POOL_ENABLED));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolInitSize",
                GlobalNames.DATASOURCE_VDP_POOL_INIT_SIZE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxActive",
                GlobalNames.DATASOURCE_VDP_POOL_MAX_ACTIVE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxIdle",
                GlobalNames.DATASOURCE_VDP_POOL_MAX_IDLE));

        dsVDP.setParameters(defaultConfig);

        return dsVDP;

    }

    private static Configuration buildJDBCDS(String dataSourceName,
            SchedulerManager schedulerManager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        MetaConfiguration metaConfig =
            schedulerManager.getMetaConfiguration("dataSource", "JDBC");

        ParameterStructure defaultConfig =
            metaConfig.getDefaultConfiguration();

        Configuration dsJDBC = new Configuration();
        dsJDBC.setDraft(false);
        dsJDBC.setType("dataSource");
        dsJDBC.setSubType("JDBC");

        defaultConfig.put(Helper.buildSimpleMonoValuedParam("name",
                dataSourceName));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolEnabled",
                GlobalNames.DATASOURCE_JDBC_POOL_ENABLED));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolInitSize",
                GlobalNames.DATASOURCE_JDBC_POOL_INIT_SIZE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxActive",
                GlobalNames.DATASOURCE_JDBC_POOL_MAX_ACTIVE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxWait",
                GlobalNames.DATASOURCE_JDBC_POOL_MAX_WAIT));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxIdle",
                GlobalNames.DATASOURCE_JDBC_POOL_MAX_IDLE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMinIdle",
                GlobalNames.DATASOURCE_JDBC_POOL_MIN_IDLE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolExhaustedAction",
                GlobalNames.DATASOURCE_JDBC_POOL_EXAUSTED_ACTION));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolTestOnBorrow",
                GlobalNames.DATASOURCE_JDBC_POOL_TEST_ON_BORROW));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolTestOnReturn",
                GlobalNames.DATASOURCE_JDBC_POOL_TEST_ON_RETURN));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolTestWhileIdle",
                GlobalNames.DATASOURCE_JDBC_POOL_TEST_WHILE_IDLE));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolTimeBetweenEviction",
                GlobalNames.DATASOURCE_JDBC_POOL_TIME_BETWEEN_EVICTION));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolNumTestPerEviction",
                GlobalNames.DATASOURCE_JDBC_POOL_NUM_TEST_PER_EVICTION));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMinEvidectableTime",
                GlobalNames.DATASOURCE_JDBC_POOL_MIN_EVIDECTABLE_TIME));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolPreparedStatements",
                GlobalNames.DATASOURCE_JDBC_POOL_PREPARED_STATEMENTS));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolMaxSleepingPreparedStatements",
                GlobalNames.DATASOURCE_JDBC_POOL_MAX_SLEEPING_PREPARED_STATEMENTS));
        defaultConfig.put(Helper.buildSimpleMonoValuedParam("poolInitialCapacityPreparedStatements",
                GlobalNames.DATASOURCE_JDBC_POOL_INITIAL_CAPACITY_PREPARED_STATEMENTS));

        dsJDBC.setParameters(defaultConfig);

        return dsJDBC;
    }

    private static Configuration buildARNIndexDS(String dataSourceName,
            SchedulerManager schedulerManager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        MetaConfiguration metaConfig =
            schedulerManager.getMetaConfiguration("dataSource", "ARN-Index");

        ParameterStructure defaultConfig =
            metaConfig.getDefaultConfiguration();

        Configuration dsArnIndex = new Configuration();
        dsArnIndex.setDraft(false);
        dsArnIndex.setType("dataSource");
        dsArnIndex.setSubType("ARN-Index");

        defaultConfig.put(Helper.buildSimpleMonoValuedParam("port",
                GlobalNames.DATASOURCE_ARN_INDEX_PORT));

        defaultConfig.put(Helper.buildSimpleMonoValuedParam("name",
                dataSourceName));

        dsArnIndex.setParameters(defaultConfig);

        return dsArnIndex;
    }

    /***    JOBS ACTIONS    ***/
    /**
     * Sets the job to enabled/disabled state. A disabled job is not
     * scheduled by the jobs scheduler system (it is ignored at load time).
     * @param jobName the name of the task.
     * @param schedulerManager the task manager.
     * @throws ConnectException if a connection error occurs. Typically,
     *         the connection was refused remotely (e.g., no process is
     *         listening on the remote address/port).
     * @throws InstanceNotFoundException if the task does not exist.
     * @throws InternalErrorException if an error occurs during the process.
     */
    public static void enableJob(String jobName, SchedulerManager schedulerManager)
        throws ConnectException, InstanceNotFoundException,
        InternalErrorException {

        Collection<JobData> jobs =
            schedulerManager.getJobsInformation();

        for (JobData job : jobs) {
            if (job.getName().compareTo(jobName) == 0 && projectId == job.getProjectId()) {
                Integer jobId = job.getJobID();
                if (job.getState().compareTo(State.DISABLED) == 0) {
                    schedulerManager.resumeJob(projectId, jobId);
                } else {
                    schedulerManager.pauseJob(projectId, jobId);
                }
            }
        }
    }

    /**
     * Stops the job.
     * @param jobName the name of the job.
     * @param schedulerManager the job manager.
     * @throws ConnectException if a connection error occurs. Typically,
     *         the connection was refused remotely (e.g., no process is
     *         listening on the remote address/port).
     * @throws InternalErrorException if an error occurs during the process.
     * @throws InstanceNotFoundException
     */
    public static void stopJob(String jobName, SchedulerManager schedulerManager)
        throws ConnectException, InternalErrorException, InstanceNotFoundException {

        Collection<JobData> jobs =
            schedulerManager.getJobsInformation();

        for (JobData job : jobs) {
            if (job.getName().compareTo(jobName) == 0 && projectId == job.getProjectId()) {
                Integer jobId = job.getJobID();
                if (job.getState().compareTo(State.RUNNING) == 0) {
                    schedulerManager.stopJob(projectId, jobId);
                }
            }
        }
    }

    public static void startJob(String jobName, SchedulerManager schedulerManager)
        throws ConnectException, InternalErrorException, InstanceNotFoundException {

        Collection<JobData> jobs =
            schedulerManager.getJobsInformation();

        for (JobData job : jobs) {
            if (job.getName().compareTo(jobName) == 0 && projectId == job.getProjectId()) {
                Integer jobId = job.getJobID();
                if ((job.getState().compareTo(State.RUNNING) != 0) &&
                        (job.getState().compareTo(State.DISABLED) != 0)) {
                    schedulerManager.startJob(projectId, jobId);
                }
            }
        }
    }

    public static void findJobNames(SchedulerManager schedulerManager)
        throws ConnectException, InternalErrorException {

        Collection<Configuration> jobs =
            schedulerManager.getElementsByType(projectId, "job");

        for (Configuration job : jobs) {
            System.out.println("Job name: " + job.getName());
        }

    }

    public static void findJob(String jobName, SchedulerManager schedulerManager)
        throws ConnectException, InternalErrorException {

        Collection<Configuration> jobs =
            schedulerManager.getElementsByType(projectId, "job");

        for (Configuration job : jobs) {
            if (job.getName().compareTo(jobName) == 0) {
                System.out.println("Job found: " + job);
            }
        }
    }

    public static void createJob(String jobName, String desc, String type,
            SchedulerManager schedulerManager) throws ConnectException,
            DuplicateInstanceException, 
            InternalErrorException, InstanceNotFoundException {

        addDataSource(jobName + "ds", schedulerManager, type);

        Configuration jobConfig = new Configuration();
        jobConfig.setDraft(false);
        jobConfig.setType("job");
        jobConfig.setSubType(type);

        ParameterStructure parameters = null;

        if (type.compareTo(TypesConventions.ARN_INDEX_SUBTYPE) == 0) {
            parameters = createARNIndexJob(jobName, desc);

        } else if (type.compareTo(TypesConventions.VDP_SUBTYPE) == 0) {
            parameters = createVDPJob(jobName, desc);

        } else if (type.compareTo(TypesConventions.JDBC_SUBTYPE) == 0) {
            parameters = createJDBCJob(jobName, desc);

        } else if (type.compareTo(TypesConventions.ITP_SUBTYPE) == 0) {
            parameters = createITPJob(jobName, desc);

        }

        if (parameters != null) {
            parameters.put(createTriggerSection());
        }

        jobConfig.setParameters(parameters);

        Configuration createConfig =
            schedulerManager.addJob(projectId, jobConfig);

        findJob(createConfig, schedulerManager);
    }

    private static Parameter<ParameterStructure> createTriggerSection() {
        Parameter<ParameterStructure> trigger = new CompoundParameter();
        trigger.setName("trigger");
        Collection<ParameterStructure> paramsLocal =
            new ArrayList<>();
        ParameterStructure param = new ParameterStructure();
        param.put(Helper.buildSimpleMonoValuedParam("type", "cron"));
        param.put(Helper.buildSimpleMonoValuedParam("cronExpression", "0 10,44 14 ? 3 WED"));
        paramsLocal.add(param);
        trigger.setValues(paramsLocal);
        return trigger;
    }

    private static ParameterStructure createJDBCJob(String jobName, String desc) {

        ParameterStructure parameters = new ParameterStructure();

        parameters.put(Helper.buildSimpleMonoValuedParam("name", jobName));
        parameters.put(Helper.buildSimpleMonoValuedParam("description", desc));
        parameters.put(Helper.buildSimpleMonoValuedParam("dataSource",
                dataSources.get(TypesConventions.JDBC_SUBTYPE)));
        parameters.put(Helper.buildSimpleMonoValuedParam("parameterizedQuery", "select * from emp"));

        return parameters;

    }

    private static ParameterStructure createVDPJob(String jobName, String desc) {

        ParameterStructure parameters = new ParameterStructure();

        parameters.put(Helper.buildSimpleMonoValuedParam("name", jobName));
        parameters.put(Helper.buildSimpleMonoValuedParam("description", desc));
        parameters.put(Helper.buildSimpleMonoValuedParam("dataSource",
                dataSources.get(TypesConventions.VDP_SUBTYPE)));
        parameters.put(Helper.buildSimpleMonoValuedParam("parameterizedQuery", "select * from emp"));

        return parameters;

    }

    private static ParameterStructure createITPJob(String jobName, String desc) {

        ParameterStructure parameters = new ParameterStructure();

        parameters.put(Helper.buildSimpleMonoValuedParam("name", jobName));
        parameters.put(Helper.buildSimpleMonoValuedParam("description", desc));
        parameters.put(Helper.buildSimpleMonoValuedParam("dataSource",
                dataSources.get(TypesConventions.ITP_SUBTYPE)));
        parameters.put(Helper.buildSimpleMonoValuedParam("wrapper", "wrapper"));
        Collection<String> outputFields = new ArrayList<>();
        outputFields.add("outputField1");
        outputFields.add("outputField2");
        parameters.put(Helper.buildSimpleMultivaluedParam("outputFields", outputFields));

        return parameters;

    }

    /**
     * @param jobName
     * @param desc
     * @param schedulerManager
     */
    private static ParameterStructure createARNIndexJob(String jobName, String desc) {

        ParameterStructure parameters = new ParameterStructure();

        parameters.put(Helper.buildSimpleMonoValuedParam("name", jobName));
        parameters.put(Helper.buildSimpleMonoValuedParam("description", desc));
        parameters.put(Helper.buildSimpleMonoValuedParam("dataSource",
                dataSources.get(TypesConventions.ARN_INDEX_SUBTYPE)));

        Collection<ParameterStructure> actions = buildActions();
        parameters.put(Helper.buildCompoundParam("action", actions));

        return parameters;

    }

    /**
     * @return
     */
    private static Collection<ParameterStructure> buildActions() {

        Collection<ParameterStructure> actions =
            new ArrayList<>();

        ParameterStructure action = new ParameterStructure();
        action.put(Helper.buildSimpleMonoValuedParam("type", "deleteDocuments"));
        action.put(Helper.buildSimpleMonoValuedParam("query", "Denodo"));
        Collection<String> indexes = new ArrayList<>();
        indexes.add("default");
        action.put(Helper.buildSimpleMultivaluedParam("index", indexes));

        actions.add(action);
        return actions;
    }

    public static void deleteJob(String jobName, SchedulerManager schedulerManager)
        throws ConnectException, InternalErrorException, InstanceNotFoundException {

        Collection<Configuration> jobs =
            schedulerManager.getElementsByType(projectId, "job");

        for (Configuration job : jobs) {
            if (job.getName().compareTo(jobName) == 0) {
                schedulerManager.removeJob(projectId, job.getId());
                System.out.println("Deleted job: " + job.getName());
            }
        }

    }

    public static void updateJobDesc(String jobName, String description,
        SchedulerManager schedulerManager) throws ConnectException,
        InternalErrorException, InstanceNotFoundException, DuplicateInstanceException,
        JobLimitExceededException {

        Collection<Configuration> jobs =
            schedulerManager.getElementsByType(projectId, "job");

        for (Configuration job : jobs) {
            if (job.getName().compareTo(jobName) == 0) {
                ParameterStructure parameters = job.getParameters();
                parameters.put(Helper.buildSimpleMonoValuedParam("description", description));
                schedulerManager.updateJob(projectId, job);
                System.out.println("Updated job: " + job.getName());
            }
        }
    }

    public static void updateJobScheduling(String jobName,
            SchedulerManager schedulerManager) throws ConnectException,
            InternalErrorException, InstanceNotFoundException, DuplicateInstanceException,
            JobLimitExceededException {

        Collection<Configuration> jobs =
            schedulerManager.getElementsByType(projectId, "job");

        for (Configuration job : jobs) {
            if (job.getName().compareTo(jobName) == 0) {
                ParameterStructure parameters = job.getParameters();
                Parameter<ParameterStructure> triggerParam = null;
                try {
                    triggerParam = parameters.get("trigger");
                    ParameterStructure tiggerParams = triggerParam.getValue();

                    String newCronExpression = "0 15 10 ? * 6#3";
                    tiggerParams.put(Helper.buildSimpleMonoValuedParam("cronExpression",
                            newCronExpression));
                    schedulerManager.updateJob(projectId, job);
                    System.out.println("Updated job: " + job.getName());
                } catch (ParameterNotFoundException e) {
                    //do nothing
                }
            }
        }
    }

    private static void findDataSource(Configuration ds, SchedulerManager manager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        Configuration element = manager.getDataSource(projectId, ds.getId());
        System.out.println(element);
    }

    private static void findJob(Configuration job, SchedulerManager manager) throws ConnectException,
            InstanceNotFoundException, InternalErrorException {

        Configuration element = manager.getJob(projectId, job.getId());
        System.out.println(element);
    }



    /***    MAIN    ***/

    public static void main(String[] args) {

        try {
            if (args.length < 3) {
                usage();
                return;
            }

            int index = 0;

            // processor option
            String optionName = args[index].toLowerCase();
            option = checkValue(optionName, options);
            if (option == -1) {
                showUnknownOption(args[index]);
                return;
            }

            // id (task, handler)
            index++;
            String id = args[index];

            // host
            index++;
            if (args.length > index) {
                if (processOptions(args, index) == -1) {
                    return;
                }
            }

            // port
            index += 2;
            if (args.length > index) {
                if (processOptions(args, index) == -1) {
                    return;
                }
            }

            // description
            index +=2;
            String description = null;
            if (args.length > index) {
                description = args[index];
            }

            // taskType
            index ++;
            String type = null;
            if (args.length > index) {
                type = args[index];
            }

            if ((host != null) && (port > -1)) {

                /* Constructs the context for locating and initializing the
                 * scheduler facade.
                 */
                Context context = ContextBuilderFactory.createContextForServerAuth(host, port, "admin", "admin", true)
                                .build();
                SchedulerManager schedulerManager = SchedulerManagerFactory.getManager(context);

                projectId = getProjectID(schedulerManager);

                //process suitable option
                switch (option) {

                case 0:
                    enableJob(id, schedulerManager);
                    break;

                case 1:
                    startJob(id, schedulerManager);
                    break;

                case 2:
                    stopJob(id, schedulerManager);
                    break;

                case 3:
                    findJobNames(schedulerManager);
                    break;

                case 4:
                    findJob(id, schedulerManager);
                    break;

                case 5:
                    createJob(id, description, type, schedulerManager);
                    break;

                case 6:
                    deleteJob(id, schedulerManager);
                    break;

                case 7:
                    updateJobDesc(id, description, schedulerManager);
                    break;

                case 8:
                    updateJobScheduling(id, schedulerManager);
                    break;

                case 9:
                    addDataSource(id, schedulerManager, description);
                    break;
                }

                System.out.println("DONE.");
            } else {
                usage();
            }

        } catch (Exception e) {
            System.out.println(" Error. Caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param schedulerManager
     * @throws ConnectException
     * @throws InternalErrorException
     */
    private static Integer getProjectID(SchedulerManager schedulerManager)
            throws ConnectException, DuplicateInstanceException, InternalErrorException {
        
        Collection<Item> projects = schedulerManager.getProjects();
        
        for (Item project : projects) {
            if (project.getName().equalsIgnoreCase(GlobalNames.PROJECT_NAME)) {
                return project.getReference();
            }
        }
        
        return schedulerManager.addProject(
                GlobalNames.PROJECT_NAME,
                GlobalNames.PROJECT_DESCRIPTION).getCurrentProject().getReference();
        
    }

}
