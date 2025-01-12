#! /bin/bash

cd "$(dirname "$0")"

source ./cleaner.sh

source script
docker build --no-cache -t security-scan ../docker/
docker tag security-scan haraleib/security-scan
#docker logout
#docker login -u ...
docker push haraleib/security-scan

$SHELL
