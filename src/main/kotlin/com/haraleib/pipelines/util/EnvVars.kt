package com.haraleib.pipelines.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object EnvVars {

  val logger: Logger = getLogger(EnvVarsTest::class.java)

  val CPU_COUNT: String = getEnvVar("CPU_COUNT", "1")
  val ENV_BITBUCKET_PR_ID: String = getEnvVar("BITBUCKET_PR_ID", "")
  val ENV_BITBUCKET_COMMIT: String = getEnvVar("BITBUCKET_COMMIT", "")
  val ENV_BITBUCKET_WORKSPACE: String = getEnvVar("BITBUCKET_WORKSPACE", "")
  val ENV_CLONE_DIR: String = getEnvVar("BITBUCKET_CLONE_DIR", "")
  val ENV_REPO_SLUG: String = getEnvVar("BITBUCKET_REPO_SLUG", "")
  val ENV_REPO_ACCESS_TOKEN: String = getEnvVar("REPOSITORY_ACCESS_TOKEN", "")
  val ENV_REPORT_OUTPUT_PATH: String = getEnvVar("REPORT_OUTPUT_PATH", "")
  val ENV_REPORT_FORMAT: String = getEnvVar("REPORT_FORMAT", "xml")

  private fun getEnvVar(envVar: String, default: String): String {
    var env = System.getenv(envVar)
    if (env == null) env = default
    /*    if (envVar == "REPOSITORY_ACCESS_TOKEN")
          deb("Accessing variable: $envVar with value: ***************")
        else*/
    deb("Accessing variable: $envVar with value: $env")
    return env
  }

  fun debug() : Boolean {
    val env = System.getenv("DEBUG")
    return if (env == null)
      true
    else env == "true"
  }
}
