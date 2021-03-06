#################################################################################################################
## Environment Name:    Admin
##
## Description:   This is a 'special' stack that MUST NOT BE INCLUDED in any specific environment as it contains
##                the storage resources for terraform remote state.  It also contains the storage resources for
##                data backups, so it really cannot be spun up automatically as part of any specific environment.
##                When adding a new environment, a new entry will need to be added here.
##
#################################################################################################################

## Resources
#################################################################################################################
module "admin" {
  source = "git::ssh://aengus123@bitbucket.org/mcculloughsolutions/ski-analytics-infrastructure.git//src/modules/admin?ref=[[${infra_ref}]]"
  lock-read-capacity = 1
  lock-write-capacity = 1
  profile = var.profile
  project_name = var.project_name
  stage = "[[${stage}]]"
  env = "[[${env}]]"
}

## Variables
#################################################################################################################
variable "profile" {
  type = string
  description = "AWS profile to use"
}
variable "project_name" {
  type = string
  description = "name of project"
}
variable "env" {
  type = string
  description = "name of environment"
}

variable "stage" {
  type = string
  description = "name of stage"
}

variable "lock-table-read-capacity" {
  type = number
  description = "lock table read capacity"
}

variable "lock-table-write-capacity" {
  type = number
  description = "lock table write capacity"
}

variable "domain_stack" {
  type = string
  description = "name of shared domain stack"
}