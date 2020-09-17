#!/bin/bash
## Script for CI / CD to automatically teardown an environment
##
## Args:  $1  name of environment to destroy
##        $2  (optional) name of module to destroy. defaults to all


## Modules to destroy //order matters!
declare -a modules=("frontend" "webclient-cd" "data")

## log file location
readonly LOG_FILE="destroy_env.log"

set -o errexit
touch $LOG_FILE
env=$1

export TF_IN_AUTOMATION=true
export TF_LOG="trace"
export TF_LOG_PATH="terraform_destroy.log"


echo "Destroying environment " $env

## Exit if environment not found
if [ -d $env ]; then
  echo "Found environment" $env " to destroy"
else
  echo "Environment "$env " to destroy not found. Exiting." >&2
  exit 1
fi

## Initialize, Plan and Apply ALL modules
for i in "${modules[@]}"; do

#  # if mod variable is set then only action that module
#  if [ -z ${mod} ]; then
#    echo "Destroy Single Module: ${i}";
#  else
#    if [ "$mod" != "$i" ]; then
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

# Terraform Destroy
  cd ${env}/$i && pwd
  terraform destroy --var-file="${env}.terraform.tfvars.json" \
   --input=false --auto-approve >>$LOG_FILE 2>&1
  if [ $? -eq 0 ]; then
    echo "Successfully destroyed terraform ${i} module"
  else
    echo "Error destroying terraform module ${i}" >&2
    exit 1
  fi
  cd ../..
done

exit 0