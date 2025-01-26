#!/usr/bin/env bats

setup() {
  DOCKER_IMAGE=${DOCKER_IMAGE:="test/security-scan"}

  echo "Building image..."
  docker build -t ${DOCKER_IMAGE}:test ./docker/
}

@test "Integration test" {
    run docker run \
        -e REPOSITORY_ACCESS_TOKEN=${SECURITY_SCAN_AUTH_TOKEN} \
        -e BITBUCKET_PR_ID='6' \
        -e BITBUCKET_COMMIT='f041523' \
        -e BITBUCKET_WORKSPACE='haraldeibensteiner' \
        -e BITBUCKET_CLONE_DIR='/opt/atlassian/pipelines/agent/build' \
        -e BITBUCKET_REPO_SLUG='security-scan' \
        -e REPORT_FORMAT='xml' \
        -e DEBUG='true' \
        -v $(pwd):$(pwd) \
        -w $(pwd) \
        ${DOCKER_IMAGE}:test

    echo "Status: $status"
    echo "Output: $output"

    [ "$status" -eq 0 ]
}

