package com.mariner.xvu;

import com.mariner.xvu.beans.Report;
import com.mariner.xvu.utils.ReportParser;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The project execution starts from this program.
 * It calls the ReportParser to parse the files from
 * csv, XML and JSON format.
 * It merges data from all the 3 files and then formats the data:-
 * a) The same column order and formatting as reports.csv
 * b) removes the records which have packetServiced = 0
 * c) sorts the record based on the requesttime(converted to UTC timezone)
 *
 * Note: Final CSV is created in output folder in context root
 *       Naming convention of final csv : reports_merged_yyyyMMDDHHmm.csv
 * The summary of report is printed in the System console
 *
 * To run this program:
 * a) install maven and run mvn -U clean install in context root
 * b) then run the jar
 *    java -jar eventfilter-1.0-SNAPSHOT-with-dependencies.jar
 *
 * @author  Ankit Mayur
 * @version 1.0
 * @since   2019-08-31
 */

public class ReportProcessorMain {

    private static final String PATH = "src/main/resources/";
    private static final String INPUT_CSV_FILE = "reports.csv";
    private static final String INPUT_JSON_FILE = "reports.json";
    private static final String INPUT_XML_FILE = "reports.xml";
    private static final String OUTPUT_XML_FILE = "./output/reports_merged";
    private static final String COMMA_DELIMTED = ",";

    /**
     * This method is the staring point of this project
     *
     * @param args : command line args
     *
     * @return : None
     */
    public static void main(String[] args) {
        //Read from CSV File
        List<Report> reportCSVList = ReportParser.parseCSV(PATH + INPUT_CSV_FILE);

        //Read from JSON
        List<Report> reportJSONList = ReportParser.parseJSON(PATH + INPUT_JSON_FILE);

        //Read from XML
        List<Report> reportXMLList = ReportParser.parseXML(PATH + INPUT_XML_FILE);

        //merge, filter, sort all files
        List<Report> reportMergedList = mergeAndFormat(reportCSVList,
                reportJSONList, reportXMLList);

        //output merged result to csv in same order as report.csv
        generateMergedResultCSV(reportMergedList, OUTPUT_XML_FILE);

        //print summary in console
        printSummaryMergedResult(reportMergedList);
    }

    /**
     * This method does following task
     * a) merge the csv, XML and JSON data into 1
     * b) remove the record where packets-serviced = 0
     * c) sort the record by request-time(in UTC timezone)
     * and return the formatted and merged list of report back
     *
     * @param csvList : csv List read from CSV file
     * @param jsonList : JSON List read from JSON file
     * @param xmlList : XML List read from XML file
     *
     * @return List<Report>: Merged , filtered, sorted list of final result
     */
    private static List<Report> mergeAndFormat(List<Report> csvList,
                                               List<Report> jsonList,
                                               List<Report> xmlList) {
        List<Report> mergedResult = new ArrayList<>();
        //consolidate records
        mergedResult.addAll(csvList);
        mergedResult.addAll(jsonList);
        mergedResult.addAll(xmlList);

        //removed record with packets serviced = 0
        List<Report> filteredResult = mergedResult.stream().filter(report ->
                Integer.valueOf(report.getPacketsServiced()) > 0)
                .collect(Collectors.toList());

        //sort
        filteredResult.sort(Comparator.comparing(Report::getRequestTime));

        return filteredResult;
    }

    /**
     * This method is creating the final CSV with following naming convention
     * Path : contextRoot/output
     * File : 'reports_merged_yyyyMMddHHmm.csv'
     *
     * @param mergedList : merged list to be printed
     * @param outputFilePrefix prefix of output file(path + filename)
     *      *
     * @return None
     */
    private static void generateMergedResultCSV(List<Report> mergedList,
                                                String outputFilePrefix) {
        StringBuilder eachLine;
        if (mergedList.isEmpty()) {
            return;
        }
        String dateSuffix =
                new SimpleDateFormat("_yyyyMMddHHmm'.csv'")
                        .format(new Date());
        String outputFilename = outputFilePrefix + dateSuffix;
        File file = new File(outputFilename);
        StringBuilder header = new StringBuilder();
        header.append("client-address,client-guid,request-time,")
                .append("service-guid,retries-request,packets-requested,")
                .append("packets-serviced,max-hole-size");
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(outputFilename)))) {
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            //Write the header first
            bufferedWriter.write(header.toString());
            bufferedWriter.newLine();
            //Write the content
            for (Report eachReport : mergedList) {
                eachLine = new StringBuilder();
                eachLine.append(eachReport.getClientAddress())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getClientGuid())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getRequestTime())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getServiceGuid())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getRetriesRequest())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getPacketsRequested())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getPacketsServiced())
                        .append(COMMA_DELIMTED)
                        .append(eachReport.getMaxHoleSize());
                bufferedWriter.write(eachLine.toString());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is printing the merged report grouped by
     * serviceGuid
     *
     * @param mergedList : merged summary to be printed
     *      Service-guid: x1x1, Count -> y1
     *      Service-guid: x2x2, Count -> y2
     *
     * @return None
     */
    private static void printSummaryMergedResult(List<Report> mergedList) {
        Map<String, Integer> serviceGuidCountMap = new HashMap<>();
        for (Report report : mergedList) {
            //if the serviceGUID exist , increment the count
            if (serviceGuidCountMap.containsKey(report.getServiceGuid())) {
                serviceGuidCountMap.put(report.getServiceGuid(),
                        serviceGuidCountMap.get(report.getServiceGuid()) + 1);
            } else {
                //Otherwise create an entry in the map
                serviceGuidCountMap.put(report.getServiceGuid(), 1);
            }
        }
        StringBuilder formatResult;
        System.out.println("*************** MERGE SUMMARY REPORT ****************");
        for (Map.Entry<String, Integer> eachEntry :
                serviceGuidCountMap.entrySet()) {
            //print the result in the console
            formatResult = new StringBuilder();
            formatResult.append("Service-guid: ")
                    .append(eachEntry.getKey())
                    .append(", Count -> ")
                    .append(eachEntry.getValue());
            System.out.println(formatResult.toString());
        }

        System.out.println("*******************************************************");
    }
}
