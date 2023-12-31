# Customer Details Processor

## Task
We will need a new Java app to read in a CSV file from a directory. The contents will then need to be sent to 
a REST API endpoint, in JSON format, and saved to a SQL database.

    1. Create a console app to read in a CSV file from a directory.
    2. Parse the CSV file of which the contents are:
        ◦ Customer Ref
        ◦ Customer Name
        ◦ Address Line 1
        ◦ Address Line 2
        ◦ Town
        ◦ County
        ◦ Country
        ◦ Postcode
    3. Loop through the rows of the CSV file and send each row to a REST API POST endpoint, in JSON format.
    4. The REST API should then save the content to a database table. Format can match the CSV file.
    5. Create a REST API GET endpoint to retrieve the customer information, passing in a customer ref to filter the data. Contents should be returned in JSON format.
    6. Some documentation or Wiki to explain the approach taken.

Where possible, a Test-Driven Development (TDD) approach should be taken.

## Introduction

* Command line application accepts a file and loads it into a web app via REST http as requested
* This is gradle multi module project where "customer-details-api" is the web application and "customer-details-file-importer" 
is a fat jar command line application. They share "customer-details-domain" for an API contract.
* UTF-8 is respected
* Blank or malformed csv records should not affect the overall processing
* Each file processing will result in report file generated in the same directory with the same file prefix

## Prerequisites

* Docker and docker compose

## How to
* Simply type "make" in your project terminal and follow the steps from 0. till last. E.g. "make 1_Run_customer-details-api_with_its_database"
using "tab" so it auto-completes step selection
* If 1. fails, run 0 (note: It will shutdown and remove all your running docker instances)

## How to verify database
Once step 1 is running, if you have "mysql-client" installed:  
mysql -h localhost -P 3306 --protocol=tcp -u root -pasfgASFA42  
USE main;  
SELECT * FROM customer;

## Whats and Whys
* This project is nearly enterprise grade, hence the complexity and why I used reactor, I needed
new up-to-date reference code for my future endeavors.
* There was no particular reason to use reactor for CMD application, I was interested in junction between
blocking and non-blocking code. The only parallel bit happening is with webClient.
* The API is fully non-blocking
