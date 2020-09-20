## Configuration
#################################################################################################################
terraform {
  backend "s3" {
    bucket = "namedenv-crunch-ski-tf-backend-store"
    key = "frontend/terraform.tfstate"
    region = "us-east-1"
    dynamodb_table = "namedenv-crunch-ski-terraform-state-lock-dynamo"
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
  source = "git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git//src/stacks/frontend?ref=develop"
  app_alias = var.app_alias
  domain_name = var.domain_name
  primary_region = var.primary_region
  profile = var.profile
  project_name = var.project_name
  stage = var.stage
  env = var.env
  domain_stack = var.domain_stack
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

variable "domain_stack" {
  type = string
  description = "name of shared domain stack"
}