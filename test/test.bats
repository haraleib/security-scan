#!/usr/bin/env bats

setup() {
  DOCKER_IMAGE=${DOCKER_IMAGE:="test/security-scan"}

  echo "Building image..."
  docker build -t ${DOCKER_IMAGE}:test ./docker/
}

@test "Dummy test" {
    run docker run \
        -e REPOSITORY_ACCESS_TOKEN=${SECURITY_SCAN_AUTH_TOKEN} \
        -e REPORT_FORMAT='xml' \
        -e DEBUG='true' \
        -v $(pwd):$(pwd) \
        -w $(pwd) \
        ${DOCKER_IMAGE}:test

    echo "Status: $status"
    echo "Output: $output"

    [ "$status" -eq 0 ]
}

