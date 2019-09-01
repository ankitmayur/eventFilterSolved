package com.mariner.xvu.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Arrays;

/**
 * Value is stored in this bean after successful
 * parsing of XML
 *
 * @author Ankit Mayur
 * @version 1.0
 * @since 2019-08-31
 */
public class Records {
    @JacksonXmlElementWrapper(localName = "report", useWrapping = false)
    private Report[] report;

    public Report[] getReport() {
        return report;
    }

    public void setReport(Report[] report) {
        this.report = report;
    }

    public Records() {
    }

    public Records(Report[] report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "Records{" +
                "report=" + Arrays.toString(report) +
                '}';
    }
}
