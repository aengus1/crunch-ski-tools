## Configuration
#################################################################################################################
terraform {
  backend "s3" {
    bucket = "namedenv-crunch-ski-tf-backend-store"
    key = "webclient-cd/terraform.tfstate"
    region = "us-east-1"
    dynamodb_table = "namedenv-crunch-ski-terraform-state-lock-dynamo"
    encrypt = false
  }
  required_providers {
    aws = "~> 2.47.0"
  }
}

provider "aws" {
  region = var.primary_region
  profile = var.profile
}


## Module
#################################################################################################################

module "cd-webclient" {
  source = "git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git/src/modules/cd-webclient?ref=develop"
  domain_name = var.domain_name
  primary_region = var.primary_region
  profile = var.profile
  project_name = var.project_name
  stage = var.stage
  env = var.env
}

## Outputs
#################################################################################################################

output "cd-user-key" {
  value = module.cd-webclient.access_key
}

output "cd-user-secret-key" {
  value = module.cd-webclient.access_key_secret
}


## Variables
#################################################################################################################
variable "project_name" {
  type = string
  description = "name of this project"
}

variable "domain_name" {
  type = string
  description = "domain name for which to create dkim records"
}

variable "primary_region" {
  type = string
  description = "aws region for acm certificate"
}

variable "profile" {
  type = string
  description = "aws profile to use"
}

variable "stage" {
  type = string
  description = "stage name"
}

variable "env" {
  type = string
  description = "environment name"
}