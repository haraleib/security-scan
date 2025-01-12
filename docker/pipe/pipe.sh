#!/usr/bin/env bash
set -x

source "$(dirname "$0")/common.sh"

export REPOSITORY_ACCESS_TOKEN=${REPOSITORY_ACCESS_TOKEN:?'REPOSITORY_ACCESS_TOKEN missing.'}
export CPU_COUNT=$(nproc)

info "Start running security scan ..."
java -jar /security-scan-runner.jar

if [[ "$?" == "0" ]]; then
  success "Success!"
else
  fail "Error!"
fi
