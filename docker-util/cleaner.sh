#! /bin/bash

docker stop security-scan
docker rm security-scan
docker rmi security-scan

docker stop haraleib/security-scan
docker rm haraleib/security-scan
docker rmi haraleib/security-scan
