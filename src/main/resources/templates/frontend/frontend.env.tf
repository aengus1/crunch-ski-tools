## Configuration
#################################################################################################################
terraform {
  backend "s3" {
    bucket = "[[${env}]]-[[${project_name}]]-tf-backend-store"
    key = "frontend/terraform.tfstate"
    region = "us-east-1"
    dynamodb_table = "[[${env}]]-[[${project_name}]]-terraform-state-lock-dynamo"
    encrypt = false
  }
  required_providers {
    aws = "~> 2.48.0"
  }
}

provider "aws" {
  region = var.primary_region
  profile = var.profile
}
## Module
#################################################################################################################

module "frontend" {
  source = "git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git?ref=[[${infra_branch}]]/src/stacks/frontend"
  app_alias = var.app_alias
  domain_name = var.domain_name
  primary_region = var.primary_region
  profile = var.profile
  project_name = var.project_name
  stage = var.stage
  env = var.env
}
## Outputs
#################################################################################################################
output "cloudfront_distro_domain" {
  value = module.frontend.cloudfront_distro_domain
}

output "s3_bucket_app" {
  value = module.frontend.s3_bucket_app
}


## Variables
#################################################################################################################
variable "primary_region" {
  type = string
  description = "primary region"
}

variable "project_name" {
  type = string
  description = "name of this project"
}

variable "domain_name" {
  type = string
  description = "domain name for which to create A records"
}

variable "profile" {
  type = string
  description = "aws profile to use"
}

variable "app_alias" {
  type = bool
  description = "if true will prefix application url with environment name"
}

variable "stage" {
  type = string
  description = "stage name"
}

variable "env" {
  type = string
  description = "environment name"
}