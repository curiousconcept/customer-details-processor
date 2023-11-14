# Customer Details Processor

## Introduction

* Command line application accepts a file and loads it into Web Application via REST http as requested
* This is gradle multi module project where "customer-details-api" is the web application and "customer-details-file-importer" 
is a fat jar command line application. They share "customer-details-domain" for an API contract.

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
