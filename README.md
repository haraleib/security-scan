# Bitbucket Pipelines Pipe: security-scan

Secuirity scan to find vulnerablities in PR and write comments

## YAML Definition

Add the following snippet to the script section of your `bitbucket-pipelines.yml` file:

```yaml

script:
  script:
    - pipe: docker://haraleib/security-scan:latest
      variables:
      REPOSITORY_ACCESS_TOKEN: ${SECURITY_SCAN_AUTH_TOKEN}
      # DEBUG: "<boolean>" # Optional
```
## Variables

| Variable                    | Usage                                                                                             |
|-----------------------------|---------------------------------------------------------------------------------------------------|
| REPOSITORY_ACCESS_TOKEN (*) | Bitbucket repository variable which contains an access-token with pull requests write permissions |
| DEBUG                       | Turn on extra debug information. Default: `false`.                                                | 

_(*) = required variable._

## Prerequisites

Repository hosted on Bitbucket Cloud.

## Examples

Basic example:

```yaml
image: atlassian/default-image:4

pipelines:
  pull-requests:
  '**':
    - step:
      name: Security Scan
      script:
        - pipe: docker://haraleib/security-scan:latest
          variables:
          REPOSITORY_ACCESS_TOKEN: ${SECURITY_SCAN_AUTH_TOKEN}
          # DEBUG: "<boolean>" # Optional
```

## Support
If you’d like help with this pipe, or you have an issue or feature request, let us know.
The pipe is maintained by Harald Eibensteiner.

If you’re reporting an issue, please include:

- the version of the pipe
- relevant logs and error messages
- steps to reproduce

## License
Copyright (c) 2019 Atlassian and others.
Apache 2.0 licensed, see [LICENSE](LICENSE.txt) file.
