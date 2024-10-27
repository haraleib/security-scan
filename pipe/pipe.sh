#!/usr/bin/env bash
#
# Security scan to find vulnerabilities in PR and write comments
#

source "$(dirname "$0")/common.sh"

info "Executing the pipe..."

# Required parameters
NAME=${NAME:?'NAME variable missing.'}

# Default parameters
DEBUG=${DEBUG:="false"}

run echo "${NAME}"

if [[ "${status}" == "0" ]]; then
  success "Success!"
else
  fail "Error!"
fi
