FROM alpine/java:21-jdk
LABEL org.opencontainers.image.authors="harald.eib@gmail.com"

RUN apk add --update --no-cache bash

#Install bitbucket necessary toolkit
RUN wget --no-verbose -P / https://bitbucket.org/bitbucketpipelines/bitbucket-pipes-toolkit-bash/raw/0.6.0/common.sh
RUN chmod a+x /common.sh

COPY ./security-scan-runner.jar /security-scan-runner.jar
COPY pipe /
RUN chmod +x /pipe.sh

RUN mkdir -p /security-report && chmod +rw /security-report

ENV REPORT_OUTPUT_PATH=/security-report
ENTRYPOINT ["/pipe.sh"]


