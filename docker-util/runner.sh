#! /bin/bash

cd "$(dirname "$0")"

source ./cleaner.sh

docker build --no-cache -t security-scan ../docker/
docker run --name security-scan security-scan -it

$SHELL
