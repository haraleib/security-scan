FROM alpine/java:21-jdk
LABEL org.opencontainers.image.authors="harald.eib@gmail.com"

RUN apk add --update --no-cache bash

#Enrich with https://bitbucket.org/bitbucketpipelines/bitbucket-pipes-toolkit/src/master/
#Install bitbucket necessary toolkit
RUN wget --no-verbose -P / https://bitbucket.org/bitbucketpipelines/bitbucket-pipes-toolkit-bash/raw/0.6.0/common.sh
RUN chmod a+x /*.sh

COPY ./security-scan-runner.jar /security-scan-runner.jar
COPY pipe /

ENV DEBUG=true
ENV REPORT_OUTPUT_PATH=/opt/atlassian/pipelines/agent/build
ENTRYPOINT ["/pipe.sh"]


