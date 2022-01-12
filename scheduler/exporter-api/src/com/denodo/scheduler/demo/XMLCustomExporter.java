/*
 * Copyright (c) 2009. DENODO Technologies.
 * http://www.denodo.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of DENODO
 * Technologies ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with DENODO.
 */
package com.denodo.scheduler.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.denodo.commons.Document;
import com.denodo.configuration.ConfigurationException;
import com.denodo.configuration.ConfigurationHelper;
import com.denodo.configuration.ParameterStructure;
import com.denodo.scheduler.api.commons.conventions.FieldsConventions;
import com.denodo.scheduler.api.exporter.Exporter;
import com.denodo.scheduler.api.exporter.ExporterContext;
import com.denodo.scheduler.api.exporter.ExporterException;
import com.denodo.scheduler.api.exporter.ExporterResource;
import com.denodo.scheduler.api.exporter.SchedulerExporter;
import com.denodo.util.exceptions.InternalErrorException;

/**
 * This custom exporter for Denodo Scheduler exports a collection of documents
 * as a list of XML elements (with no root element).
 * <p>
 * Binary data is encoded in Base64 and compound fields are translated to XML
 * elements.
 */
public class XMLCustomExporter implements Exporter, SchedulerExporter {

    private static final String BLANKS_4 = "    ";

    /* Path: single-valued parameter */
    private static final String PATH_PARAMETER = "Output XML file absolute path";

    /* Verbose: single-valued parameter */
    private static final String VERBOSE_PARAMETER = "Export job identifier, job name, project name and execution time fields";

    private String path;
    private Boolean verbose;
    private ExporterContext context;
    private FileOutputStream fop;

    /**
     * Exports, as a XML file, the collection of documents passed as a
     * parameter.
     * 
     * @param documents
     *            the documents
     */
    @Override
    public void export(Collection documents) throws ExporterException {
        try {
            fop.write(getXMLString(documents).getBytes());
            fop.flush();
        } catch (FileNotFoundException e) {
            throw new ExporterException(e);
        } catch (IOException e) {
            throw new ExporterException(e);
        }
    }

    /**
     * Transforms a collection of documents into a XML string.
     * 
     * @param documents
     *            the documents
     * @return the XML string.
     */
    private String getXMLString(Collection documents) {

        String out = "";

        for (Object o : documents) {
            out = out.concat("<DOCUMENT>\n");
            Document tuple = (Document) o;
            tuple = removeRuntimeInfoFromDocument(tuple);
            Map fields = tuple.getFields();
            for (Object k : fields.keySet()) {
                String elementName = (String) k;
                if (verbose) {
                    out = out.concat(getXMLString(fields, elementName));
                } else {
                    if (elementName
                            .compareTo(FieldsConventions.DOCUMENT_IDENTIFIER_FIELD) != 0) {
                        out = out.concat(getXMLString(fields, elementName));
                    }
                }
            }
            if (verbose) {
                for (Object k : context.getContext().keySet()) {
                    String elementName = (String) k;
                    out = out.concat(getXMLString(context.getContext(),
                            elementName));
                }
            }
            out = out.concat("</DOCUMENT>\n");
        }

        return out;
    }

    /**
     * Transforms a document field into XML.
     * 
     * @param field
     *            the field
     * @param name
     *            the field's name
     * @return the updated XML string
     */
    private String getXMLString(Map fields, String name) {

        String out = "";
        String elementName = name;

        if (elementName.compareTo(FieldsConventions.JOBID_FIELD) == 0) {
            elementName = "JOB_ID";
        }
        if (elementName.compareTo(FieldsConventions.JOBNAME_FIELD) == 0) {
            elementName = "JOB_NAME";
        }
        if (elementName.compareTo(FieldsConventions.JOBPROJECT_FIELD) == 0) {
            elementName = "JOB_PROJECT";
        }
        if (elementName.compareTo(FieldsConventions.JOBRETRYCOUNT_FIELD) == 0) {
            elementName = "JOB_RETRY_COUNT";
        }
        if (elementName.compareTo(FieldsConventions.JOBRETRYSTARTTIME_FIELD) == 0) {
            elementName = "JOB_RETRY_START_TIME";
        }
        if (elementName.compareTo(FieldsConventions.JOBSTARTTIME_FIELD) == 0) {
            elementName = "JOB_START_TIME";
        }
        if (elementName.compareTo(FieldsConventions.DOCUMENT_IDENTIFIER_FIELD) == 0) {
            elementName = "DOC_ID";
        }

        Object value = fields.get(name);
        String elementValue = "";

        if (value != null) {
            if (value.getClass().getCanonicalName().compareTo("byte[]") == 0) {
                String elementName2 = elementName.substring(0,
                        elementName.length()).concat(" encoding=\"base64\"");
                elementValue = new String(Base64.encodeBase64((byte[]) value));
                if (elementValue.endsWith("\n")) {
                    out = out.concat(BLANKS_4).concat("<").concat(elementName2)
                            .concat(">").concat(elementValue).concat(BLANKS_4)
                            .concat("</").concat(elementName).concat(">\n");
                } else {
                    out = out.concat(BLANKS_4).concat("<").concat(elementName2)
                            .concat(">").concat(elementValue).concat("</")
                            .concat(elementName).concat(">\n");
                }
            } else {
                if (value.getClass().getCanonicalName()
                        .compareTo("java.util.ArrayList") == 0) {
                    elementValue = "\n".concat(getXMLString((ArrayList) value,
                            8));
                } else if (value.getClass().getCanonicalName()
                        .compareTo("java.util.LinkedHashMap") == 0) {
                    elementValue = "\n".concat(getXMLString(
                            (LinkedHashMap) value, 8));
                } else if (value.getClass().getCanonicalName()
                        .compareTo("java.lang.String") == 0) {
                    elementValue = "<![CDATA[".concat(
                            ((String) value)
                                    .replace("]]\\>", "]]]]><![CDATA[>"))
                            .concat("]]>");
                } else {
                    elementValue = value.toString();
                }
                if (elementValue.endsWith("\n")) {
                    out = out.concat(BLANKS_4).concat("<").concat(elementName)
                            .concat(">").concat(elementValue).concat(BLANKS_4)
                            .concat("</").concat(elementName).concat(">\n");
                } else {
                    out = out.concat(BLANKS_4).concat("<").concat(elementName)
                            .concat(">").concat(elementValue).concat("</")
                            .concat(elementName).concat(">\n");
                }
            }
        }

        return out;
    }

    /**
     * Transforms an array of registers into XML.
     * 
     * @param elements
     *            the array
     * @param numBlanks
     *            initial level of indentation
     * @return the XML string
     */
    private String getXMLString(ArrayList elements, int numBlanks) {

        String out = "";

        for (Object element : elements) {
            String blanks = getBlanks(numBlanks);
            String out2 = blanks
                    .concat("<ITEM>\n")
                    .concat(getXMLString((LinkedHashMap) element, numBlanks + 4))
                    .concat(blanks).concat("</ITEM>\n");
            out = out.concat(out2);
        }

        return out;
    }

    /**
     * Transforms a register into XML.
     * 
     * @param register
     *            the register
     * @param numBlanks
     *            initial level of indentation
     * @return the XML string
     */
    private String getXMLString(LinkedHashMap register, int numBlanks) {

        String out = "";
        String blanks = getBlanks(numBlanks);

        for (Object entry : register.entrySet()) {
            String elementName = ((Map.Entry<String, Object>) entry).getKey();
            Object elementValue = ((Map.Entry<String, Object>) entry)
                    .getValue();
            String value = "";
            if (elementValue.getClass().getCanonicalName().compareTo("byte[]") == 0) {
                String elementName2 = elementName.substring(0,
                        elementName.length()).concat(" encoding=\"base64\"");
                value = new String(Base64.encodeBase64((byte[]) elementValue));
                if (value.endsWith("\n")) {
                    out = out.concat(blanks).concat("<").concat(elementName2)
                            .concat(">").concat(value).concat(blanks)
                            .concat("</").concat(elementName).concat(">\n");
                } else {
                    out = out.concat(blanks).concat("<").concat(elementName2)
                            .concat(">").concat(value).concat("</")
                            .concat(elementName).concat(">\n");
                }
            } else {
                if (elementValue.getClass().getCanonicalName()
                        .compareTo("java.lang.String") == 0) {
                    value = "<![CDATA[".concat(
                            ((String) elementValue).replace("]]\\>",
                                    "]]]]><![CDATA[>")).concat("]]>");
                } else if (elementValue.getClass().getCanonicalName()
                        .compareTo("java.util.ArrayList") == 0) {
                    value = "\n".concat(getXMLString((ArrayList) elementValue,
                            numBlanks + 4));
                } else if (elementValue.getClass().getCanonicalName()
                        .compareTo("java.util.LinkedHashMap") == 0) {
                    value = "\n".concat(getXMLString(
                            (LinkedHashMap) elementValue, numBlanks + 4));
                } else {
                    value = elementValue.toString();
                }
                if (value.endsWith("\n")) {
                    out = out.concat(blanks).concat("<").concat(elementName)
                            .concat(">").concat(value).concat(blanks)
                            .concat("</").concat(elementName).concat(">\n");
                } else {
                    out = out.concat(blanks).concat("<").concat(elementName)
                            .concat(">").concat(value).concat("</")
                            .concat(elementName).concat(">\n");
                }
            }
        }

        return out;
    }

    /**
     * Returns a string made of blanks (as many as specified)
     * 
     * @param numBlanks
     *            the number of blanks
     * @return a string with numBlanks blanks
     */
    private String getBlanks(int numBlanks) {
        String blanks = "";
        for (int i = 0; i < numBlanks; i++) {
            blanks = blanks.concat(" ");
        }
        return blanks;
    }

    /**
     * Returns the exporter's name.
     * 
     * @return the name
     */
    @Override
    public String getName() {
        return "XMLCustomExporter";
    }

    /**
     * Initialize method. The following configuration parameters are required:
     * <ul>
     * <li>PATH_PARAMETER: the absolute path to the desired XML output file</li>
     * <li>VERBOSE_PARAMETER: if set to <code>true</code>, key fields will be
     * exported</li>
     * </ul>
     * 
     * @param params
     *            the configuration parameters
     */
    @Override
    public void init(ParameterStructure params) throws ConfigurationException,
            ConnectException, InternalErrorException {

        path = ConfigurationHelper.getMandatoryParameterAsString(
                PATH_PARAMETER, params);
        verbose = ConfigurationHelper.getOptionalParameterAsBoolean(
                VERBOSE_PARAMETER, false, params);
    }

    /**
     * Sets the exporter's context (runtime information about the extraction
     * job) and opens a file output stream which won't be closed until
     * {@link XMLCustomExporter#close()} is called.
     */
    @Override
    public void open(ExporterContext context) throws ExporterException {

        this.context = context;

        File file = new File(path);
        path = file.getPath();
        if (path.contains(File.separator)) {
            File dirs = new File(path.substring(0,
                    path.lastIndexOf(File.separator)));
            dirs.mkdirs();
        }

        fop = null;

        try {
            fop = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new ExporterException(e);
        }
    }

    /**
     * Closes the file output stream used by the exporter during its activity.
     */
    @Override
    public Collection<ExporterResource> close() throws ExporterException {

        try {
            if (fop != null) {
                fop.close();
            }
        } catch (IOException e) {
            throw new ExporterException(e);
        }

        return new ArrayList<>();
    }

    private static Document removeRuntimeInfoFromDocument(Document document) {

        Document newDocument = new Document(document);

        Iterator<String> it = newDocument.getFields().keySet().iterator();
        while (it.hasNext()) {
            String field = it.next();
            if (field.compareTo(FieldsConventions.JOBID_FIELD) == 0
                    || field.compareTo(FieldsConventions.JOBPROJECT_FIELD) == 0
                    || field.compareTo(FieldsConventions.JOBNAME_FIELD) == 0
                    || field.compareTo(FieldsConventions.JOBSTARTTIME_FIELD) == 0
                    || field.compareTo(FieldsConventions.JOBRETRYCOUNT_FIELD) == 0
                    || field.compareTo(FieldsConventions.JOBRETRYSTARTTIME_FIELD) == 0) {
                it.remove();
            }
        }

        return newDocument;
    }

}
