# Crunch Ski Tools

[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![CircleCI](https://circleci.com/bb/mcculloughsolutions/ski-analytics-tools.svg?style=shield&circle-token=a0823048b09db1d607e79fbfd7a45c63b7274a96)](https://app.circleci.com/pipelines/bitbucket/mcculloughsolutions/ski-analytics-tools)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/66768fe752444c7081f59caf63115c42)](https://www.codacy.com?utm_source=bitbucket.org&amp;utm_medium=referral&amp;utm_content=mcculloughsolutions/ski-analytics-tools&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/66768fe752444c7081f59caf63115c42)](https://www.codacy.com?utm_source=bitbucket.org&utm_medium=referral&utm_content=mcculloughsolutions/ski-analytics-tools&utm_campaign=Badge_Coverage)

## What is it?
A commandline interface for provisioning AWS environments for the
 [Ski Analytics Services](https://bitbucket.org/aengus123/ski-analytics-services/) project.


## But Why?

This tool is used in development and in the Continuous Integration Pipeline of the 
 [Ski Analytics Services](https://bitbucket.org/aengus123/ski-analytics-services/) project.  Originally these tools 
 formed a part of that project but have been ported to their own repo to take advantage of an automated 
 build pipeline to create the docker image and to simplify the services project.  Plus it was a bit too meta having the
 tools to build the services project embedded in the build of that project.
 
 ## Run modes
-  As a java utility program to manage deployment environments + data
-  As a complete development environment for ski-analytics-services (via docker)
-  As part of the CI pipeline for 

## Functionality 


#### Backup & Restore

-  Full database backup & restore
-  Per user account backup & restore
-  Backup to local storage or a designated S3 bucket


#### Status Reporting

-  Show the current deployment status of a given environmentDefinition

#### Provisioning  & Deprovisioning

-  Provision an environmentDefinition from scratch
-  Update a running environmentDefinition with only modules that have changed
-  De-provision an environmentDefinition & clean up resources

## Docker Image
To use the docker image:
`docker run -e BB_PASS=<password> -it aengus/crunch-ski-tools:LATEST /bin/sh`
<password> - password to bitbucket account with rw access to (only) the environmentDefinition repository

When using the docker container from CI ensure that on startup the environmentDefinition variable is passed in.  
See [circleci docs](https://circleci.com/docs/2.0/env-vars/#setting-an-environmentDefinition-variable-in-a-container)

## Setup
There are a few tools that need to be manually configured to run this project.

### Commitizen
Tool used to enforce commit message format.  Use the `commit` script to commit.

`npm i -g commitizen`

### Enable Annotation Processing  
1.  IntelliJ -> Preferences -> Build, Execution , Deployment -> Compiler
-  Enable Annotation processing
-  Obtain processors from project classpath

2.  Install IDE plugin