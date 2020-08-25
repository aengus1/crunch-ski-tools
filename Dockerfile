#################################################################################################################
##                  Dockerfile for Crunch Ski Tools
##
##  This image contains the tools required to build and run the project.  It is used as a
##  custom docker image for the build and deploy phases in the CI pipeline, and can also be used in development
##  to configure and manage environments.
##
#################################################################################################################

##TODO -> INSTALL PROTOBUF COMPILER https://github.com/protocolbuffers/protobuf/releases/protoc-3.13.0-linux-x86_64.zip

FROM centos/nodejs-8-centos7:latest

WORKDIR /usr/local/app
USER root

ENV TERRAFORM_VERSION="0.12.2"
ENV NODE_VERSION="13.x"
ENV JAVA_VERSION="11"
ENV GRADLE_VERSION="6.2.2"

# Update Yum repo
RUN yum -y update && yum clean all

## Install CircleCI required packages
RUN yum -y install git ssh tar gzip ca-certificates wget maven

## Install Node / NPM
RUN yum install -y gcc-c++ make && yum clean all
RUN curl -sL https://rpm.nodesource.com/setup_${NODE_VERSION} |  bash -
RUN yum -y install nodejs
# CMD node -v

## Install Serverless Framework
RUN ["npm", "install", "--global", "--unsafe-perm", "serverless"]
RUN ["ln", "-s", "/opt/app-root/src/.npm-global/bin/serverless", "/usr/local/bin/serverless"]
# CMD ["sls", "--version"]

## Install OpenJDK
RUN yum install -y java-${JAVA_VERSION}-openjdk-devel && yum clean all
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-11*
ENV PATH=${JAVA_HOME}/bin:${PATH}
RUN alternatives --set java java-${JAVA_VERSION}-openjdk.x86_64
RUN alternatives --set javac java-${JAVA_VERSION}-openjdk.x86_64
# CMD ["java", "--version"]
# CMD ["javac", "--version"]

## Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-${GRADLE_VERSION}-bin.zip
ENV PATH=/opt/gradle/gradle-${GRADLE_VERSION}/bin:${PATH}
# CMD ["gradle", "--version"]

## Install AWS CLI
RUN yum --enablerepo=extras install -y epel-release
RUN yum install -y python-pip && yum clean all
RUN pip install --upgrade pip
RUN pip install awscli
# CMD ["aws", "--version"]

## Install Terraform
RUN cd /tmp && wget https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip
RUN unzip /tmp/terraform_${TERRAFORM_VERSION}_linux_amd64.zip -d /usr/local/bin
# CMD ["terraform", "-v"]

## Install Tools Executable
RUN mkdir /usr/local/crunch
COPY build/libs/tools*.jar /usr/local/crunch/tools.jar
RUN echo "#!/bin/bash" > /usr/local/bin/crunch
RUN echo "java -jar /usr/local/crunch/tools.jar \$@" >> /usr/local/bin/crunch && chmod a+x /usr/local/bin/crunch
# CMD ["crunch --version"]

## Switch away from root user
RUN groupadd -r cigroup
RUN useradd --no-log-init -r -g cigroup circleci
RUN mkdir /home/circleci && mkdir /home/circleci/repo && mkdir /home/circleci/.ssh && cd /home/circleci
RUN chown -R circleci /home/circleci
RUN chgrp -R cigroup /home/circleci
RUN chmod -R 777 /home/circleci
ENV HOME="/home/circleci"
USER circleci