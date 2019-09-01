package com.mariner.xvu.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mariner.xvu.beans.Records;
import com.mariner.xvu.beans.Report;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the Parser util for parsing the Reports
 * it parses the XML, JSON and CSV.
 * It calls the DateUtil for converting all dates
 * to UTC timezone
 *
 * @author  Ankit Mayur
 * @version 1.0
 * @since   2019-08-31
 */
public class ReportParser<T> {

    /**
     * This method is used to parse the XML
     * It uses Jackson Library to parse the element
     *
     * @param filename Name of file(path + filename) to be parsed
     * @return List of reports loaded from the XML
     */
    public static List<Report> parseXML(String filename) {
        String line;
        List<Report> result = new ArrayList<>();
        try {
            byte[] xmlData = Files.readAllBytes(Paths.get(filename));
            //create ObjectMapper instance
            ObjectMapper objectMapper = new XmlMapper();
            //convert xml string to object
            Records records =
                    objectMapper.readValue(StringUtils.toEncodedString(xmlData,
                            StandardCharsets.UTF_8), Records.class);
            if (records != null && records.getReport() != null) {
                result = Arrays.asList(records.getReport());
                for (Report eachReport : result) {
                    eachReport.setRequestTime(DateUtil.convertADTToUTC(eachReport.getRequestTime()));
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return result;
    }

    /**
     * This method is used to parse the CSV
     * It uses Native Java Library to parse the csv
     *
     * @param fileName Name of file(path + filename) to be parsed
     * @return List of reports loaded from the CSV
     */
    public static List<Report> parseCSV(String fileName) {
        String line;
        File inputCSVFile = new File(fileName);
        List<Report> result = new ArrayList<>();
        boolean headerParsed = false;
        try (BufferedReader bf = new BufferedReader(
                new FileReader(inputCSVFile))) {
            while ((line = bf.readLine()) != null) {
                if (!headerParsed) {
                    headerParsed = true;
                    continue;
                }
                String[] splitted = line.split(",");
                Report data = new Report();
                data.setClientAddress(splitted[0]);
                data.setClientGuid(splitted[1]);
                data.setRequestTime(DateUtil.convertADTToUTC(splitted[2]));
                data.setServiceGuid(splitted[3]);
                data.setRetriesRequest(splitted[4]);
                data.setPacketsRequested(splitted[5]);
                data.setPacketsServiced(splitted[6]);
                data.setMaxHoleSize(splitted[7]);

                result.add(data);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return result;
    }

    /**
     * This method is used to parse the JSON
     * It uses Jackson Library to parse the element
     *
     * @param filename Name of file(path + filename) to be parsed
     * @return List of reports loaded from the JSON
     */
    public static List<Report> parseJSON(String filename) {
        String line;
        List<Report> result = new ArrayList<>();
        //read json file data to String
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(filename));
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //convert json string to object
            result = objectMapper.readValue(jsonData,
                    new TypeReference<List<Report>>() {
                    });
            for (Report eachReport : result) {
                eachReport.setRequestTime(
                        DateUtil.getTimeStampFromEpoch(eachReport.getRequestTime()));
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return result;
    }
}
