## Configuration
#################################################################################################################
terraform {
  backend "s3" {
    bucket = "[[${env}]]-[[${project_name}]]-tf-backend-store"
    key = "api/terraform.tfstate"
    region = "us-east-1"
    dynamodb_table = "[[${env}]]-[[${project_name}]]-terraform-state-lock-dynamo"
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

module "api" {
  source = "git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git//src/stacks/api?ref=[[${infra_ref}]]"
  api_sub_domain = var.api_sub_domain
  cognito_sub_domain = var.cognito_sub_domain
  domain_name = var.domain_name
  primary_region = var.primary_region
  profile = var.profile
  project_name = var.project_name
  ws_sub_domain = var.ws_sub_domain
  stage = var.stage
  env = var.env
  domain_stack = var.domain_stack
}

## Outputs
#################################################################################################################

output "api_endpoint_cf_domain_name" {
  value = module.api.ws_endpoint_cf_domain_name
}

output "api_endpoint_zone_id" {
  value = module.api.api_endpoint_zone_id
}

output "ws_endpoint_cf_domain_name" {
  value = module.api.ws_endpoint_cf_domain_name
}

output "ws_endpoint_zone_id" {
  value = module.api.ws_endpoint_zone_id
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

variable "cognito_sub_domain"  {
  type = string
  description = "subdomain for the cognito endpoint"
}

variable "ws_sub_domain"  {
  type = string
  description = "subdomain for the websocket endpoint"
}

variable "api_sub_domain"  {
  type = string
  description = "subdomain for the websocket endpoint"
}

variable "domain_stack" {
  type = string
  description = "name of shared domain stack"
}