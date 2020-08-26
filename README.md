# Crunch Ski Tools

[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![CircleCI](https://circleci.com/bb/mcculloughsolutions/ski-analytics-tools.svg?style=shield&circle-token=a0823048b09db1d607e79fbfd7a45c63b7274a96)](https://app.circleci.com/pipelines/bitbucket/mcculloughsolutions/ski-analytics-tools)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/d2358de026c9477d80c8acb8beae37d2)](https://www.codacy.com?utm_source=bitbucket.org&amp;utm_medium=referral&amp;utm_content=mcculloughsolutions/ski-analytics-tools&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/d2358de026c9477d80c8acb8beae37d2)](https://www.codacy.com?utm_source=bitbucket.org&utm_medium=referral&utm_content=mcculloughsolutions/ski-analytics-tools&utm_campaign=Badge_Coverage)

## What is it?
A commandline interface for provisioning AWS environments for the
 [Ski Analytics Services](https://bitbucket.org/aengus123/ski-analytics-services/) project.


## But Why?

These tools are used in development but also in the Continous Integration Pipeline of the 
 [Ski Analytics Services](https://bitbucket.org/aengus123/ski-analytics-services/) project.  Originally these tools 
 formed a part of that project but have been ported to their own repo to take advantage of an automated 
 build pipeline to create the docker image and to simplify the services project.  Plus it was a bit too meta having the
 tools

## Functionality 


#### Backup & Restore

-  Full database backup & restore
-  Per user account backup & restore
-  Backup to local storage or a designated S3 bucket


#### Status Reporting

-  Show the current deployment status of a given environment

#### Provisioning  & Deprovisioning

-  Provision an environment from scratch
-  Update a running environment with only modules that have changed
-  De-provision an environment & clean up resources

## Docker Image
To use the docker image:
`docker run -it aengus/crunch-ski-tools:LATEST /bin/sh`

## Setup
There are a few tools that need to be manually configured to run this project.

#### Commitizen
Tool used to enforce commit message format.  Use the `commit` script to commit.

`npm i -g commitizen`

#### Enable Annotation Processing  
1.  IntelliJ -> Preferences -> Build, Execution , Deployment -> Compiler
-  Enable Annotation processing
-  Obtain processors from project classpath

2.  Install IDE plugin