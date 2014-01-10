CDPS
====

Cardio prevention solution performs data analysis for individuals based on multiple sets of data, with the intent of reducing heart related risk of severe diseases and to improve personal wellness. When risks are identified, lifestyle adjustments, specific medication or further medical investigations are recommended to the user.

In order to run the web app use the following URL: http://localhost:10000

Mapreduce saves reports data as (locationId, noOfPatients) in the summary_user table.

hbase shell
  create 'summary_user', {NAME=>'details', VERSIONS=>1}
