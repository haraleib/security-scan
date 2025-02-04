package com.haraleib.pipelines

import com.haraleib.pipelines.api.SecurityScanViolation
import com.haraleib.pipelines.bitbucket.Bitbucket
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.create.CreateCommentPayload
import com.haraleib.pipelines.diff.DiffExtractor
import com.haraleib.pipelines.diff.LineChange
import com.haraleib.pipelines.history.PullRequestsOfCommitReceiver
import com.haraleib.pipelines.report.Report
import com.haraleib.pipelines.report.ReportSettings
import com.haraleib.pipelines.util.EnvVars.CPU_COUNT
import com.haraleib.pipelines.util.EnvVars.ENV_BITBUCKET_COMMIT
import com.haraleib.pipelines.util.EnvVars.ENV_BITBUCKET_PR_ID
import com.haraleib.pipelines.util.EnvVars.ENV_BITBUCKET_WORKSPACE
import com.haraleib.pipelines.util.EnvVars.ENV_CLONE_DIR
import com.haraleib.pipelines.util.EnvVars.ENV_REPORT_FORMAT
import com.haraleib.pipelines.util.EnvVars.ENV_REPORT_OUTPUT_PATH
import com.haraleib.pipelines.util.EnvVars.ENV_REPO_ACCESS_TOKEN
import com.haraleib.pipelines.util.EnvVars.ENV_REPO_SLUG
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.inf
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.nio.file.Path
import java.util.concurrent.Executors


object Main {

  val logger: Logger = getLogger(Main::class.java)

  private val pullRequestId = ENV_BITBUCKET_PR_ID.ifEmpty {
    retrievePrId(ENV_BITBUCKET_WORKSPACE, ENV_REPO_SLUG, ENV_BITBUCKET_COMMIT, ENV_REPO_ACCESS_TOKEN)
      .also { deb("PR id obtained via commit. PR id is $it") }
  }

  private val bitbucketSettings = BitbucketSettings(
    ENV_BITBUCKET_WORKSPACE,
    ENV_REPO_SLUG,
    pullRequestId,
    ENV_BITBUCKET_COMMIT,
    ENV_REPO_ACCESS_TOKEN
  )

  val bitbucket = Bitbucket(bitbucketSettings)

  private val reportSettings = ReportSettings(Path.of(ENV_CLONE_DIR), Path.of(ENV_REPORT_OUTPUT_PATH), ENV_REPORT_FORMAT)
  private val report = Report(reportSettings)

  private val cpuCount = CPU_COUNT

  @JvmStatic
  fun main(args: Array<String>) {
    inf(
      "Start evaluating for Repository: ${bitbucketSettings.repoSlug}, " +
        "Pull Request ID: ${bitbucketSettings.pullRequestId}, " +
        "Commit: ${bitbucketSettings.commit} ..."
      )

    inf("Start extracting diff ...")
    val changedLines = extractDiff()

    inf("Start analyzing ...")
    analyze(changedLines)

    inf("Start collecting violations ...")
    val violations = collectViolations(changedLines)

    inf("Start deleting old comments ...")
    deletePreviousComments();

    if (violations.isEmpty()) {
      inf("No violations found on PR ...")
      inf("Remove request changes on bitbucket PR ...")
      bitbucket.removeRequestChanges()
    } else {
      inf("Start processing comments ...")
      createComments(violations)

      inf("Request changes on bitbucket PR ...")
      bitbucket.requestChanges()
    }
  }

  private fun extractDiff(): List<LineChange> {
    val payload = bitbucket.receiveDiff()
    //deb(payload.build())
    val changedLines = DiffExtractor.extractChangedLines(payload)

    deb("The following diff could be extracted. Number of changed lines: ${changedLines.size} ...")
    //TODO do debugging directly in DiffExtractor
    for (change in changedLines) {
      deb(change.toString())
    }

    return changedLines
  }

  private fun retrievePrId(workspace: String, repoSlug: String, commit: String, repoAccessToken: String): String {
    val pullRequestReceiver = PullRequestsOfCommitReceiver(workspace, repoSlug, commit, repoAccessToken)
    val payload = pullRequestReceiver.receive()
    return payload.build()
  }

  private fun analyze(changedLines: List<LineChange>) {
    val filePaths = changedLines.map { it.filePath }.distinct()
    report.analyze(filePaths)
  }

  private fun collectViolations(changedLines: List<LineChange>): Set<SecurityScanViolation> {
    val violations = report.collectValidations(changedLines)
    deb("Found ${violations.size} violations ...")
    return violations
  }

  private fun deletePreviousComments() {
    val comments = bitbucket.receiveComments()
    val commentIds = comments.extractCommentIds()
    batchExecute(commentIds) { bitbucket.removeComment(it) }
  }

  private fun createComments(violations: Set<SecurityScanViolation>) {
    val comments = violations.map { CreateCommentPayload(it) }
    batchExecute(comments) { bitbucket.createComment(it) }
  }

  private fun <T> batchExecute(elements: Collection<T>, consumer: (T) -> Unit) {
    val executor = Executors.newFixedThreadPool(cpuCount.toInt())
    elements.forEach { element ->
      executor.execute {
        consumer(element)
      }
    }.also {
      executor.shutdown()
    }
  }
}
