#!/bin/bash
## Script for CI / CD to automatically initialize, plan and apply terraform commands for a set of modules that
## collectively make up an environment.  Main Terraform Output / Error is redirected to log file
##
## Args:
####     $1  name of environment to initialize
####     $2 (optional) name of specific module to init.  If unset will init all modules


## Modules to init //order matters!
declare -a modules=("admin" "data" "frontend" "webclient-cd")

export TF_IN_AUTOMATION=true
export TF_LOG="trace"
export TF_LOG_PATH="terraform.log"

set -o errexit

## log file location
readonly LOG_FILE="init_env.log"
touch $LOG_FILE
cat /dev/null > $LOG_FILE

env=$1
#mod=$2


echo "Initializing environment " $env


## Exit if env is not set
if [ -d $env ]; then
  echo "Found environment" $env
else
  echo "Environment "$env " not found. Exiting." >&2
  exit 1
fi

## Initialize, Plan and Apply ALL modules
for i in "${modules[@]}"; do

  # if mod variable is set then only action that module
#  if [ -z ${mod} ]; then
#    echo "Init Single Module ${i}";
#  else
#    if [ $mod != ${i} ]; then
#      continue
#      fi
#  fi

  # Confirm that the specified module's directory exists
  if [ -d ${env}/$i ]
  then
    echo "Directory ${env}/$i exists"
  else
    echo "Error: Directory ${env}/$i does not exist"
  fi

  ## Terraform Init
  cd ${env}/$i && pwd
  terraform init >>$LOG_FILE 2>&1
  if [ $? -eq 0 ]; then
    echo "Successfully initialized terraform ${i} module"
  else
    echo "Error initializing terraform" >&2
    exit 1
  fi

  ## Terraform Plan
  terraform plan --var-file="${env}.terraform.tfvars.json" \
  --input=false --out=tfplan_${i} 2>&1 | tee -a $LOG_FILE
  if [ $? -ne 0 ]; then
    echo "Error running tf plan for ${i} module" >&2
    exit 1
  fi
  ## Terraform Apply
  terraform apply --input=false --auto-approve tfplan_${i} >>$LOG_FILE
  if [ $? -eq 0 ]; then
    echo "Successfully provisioned terraform ${i} module"
  else
    ## errors occur here often due to SSM tooManyUpdates error.  The solution is simply to re-try the apply.
    ## TODO -> detect SSM TooManyUpdates error and retry
    echo "Error initializing terraform ${i} module" >&2
    exit 1
  fi
  cd ../..
done

exit 0