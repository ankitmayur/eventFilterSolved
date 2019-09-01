# eventFilterSolved
This program reads input from 3 files:-
a) csv
b) JSON
c) XML
These input files are stored at src/main/resources

It merges the result together into 1 csv with following characteristic:-
  i) The same column order and formatting as reports.csv
 ii) All report records with packets-serviced equal to zero should be excluded
iii) Records should be sorted by request-time in ascending order
Location of new file : context-root/output
Name of the ner consolidated file: reports_merged_yyyyMMDDHHmm.csv

Additionally, the application will print a summary showing the number
of records in the output file associated with each service-guid
in format: Service-guid: d7d6b499-509a-4c08-b95e-68d9662f25ca, Count -> 9

This project uses Jackson library to parse XML and JSON easily using annotations.
and Uses native Java library to parse and write CSV.

How to run this Project:
1) Install maven and run 'mvn -U clean install' in the context-root
   This will create the jar artifact in the target folder
2) then run "java -jar eventfilter-1.0-SNAPSHOT-jar-with-dependencies.jar"
   to execute the merge and view summary in console


