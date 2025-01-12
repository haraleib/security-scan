package com.haraleib.pipelines.util

import com.haraleib.pipelines.Main
import com.haraleib.pipelines.approvals.RemoveRequestChangesSender
import com.haraleib.pipelines.approvals.RequestChangesSender
import com.haraleib.pipelines.bitbucket.Bitbucket
import com.haraleib.pipelines.comments.CommentsNextPagePayload
import com.haraleib.pipelines.comments.CommentsPaginatedPayload
import com.haraleib.pipelines.comments.CommentsPayload
import com.haraleib.pipelines.comments.CommentsReceiver
import com.haraleib.pipelines.create.CreateCommentSender
import com.haraleib.pipelines.diff.DiffReceiver
import com.haraleib.pipelines.history.PullRequestsOfCommitReceiver
import com.haraleib.pipelines.pmd.PMDAnalyzer
import com.haraleib.pipelines.pmd.PMDViolationsCollector
import com.haraleib.pipelines.remove.RemoveCommentSender
import com.haraleib.pipelines.util.EnvVars.debug
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.slf4j.Logger
import se.bjurr.violations.lib.model.Violation

fun Main.inf(message: String) = logger.info(message)
fun Main.deb(message: String) = logDebug(logger, message)
fun Main.deb(message: String, violations: Set<Violation>) {
  if (debug()) {
    logger.debug(message)
    for (violation in violations) {
      logger.debug("{} \n", violation)
    }
  }
}

fun Bitbucket.inf(message: String) = logger.info(message)
fun Bitbucket.err(message: String) = logger.error(message)

fun EnvVars.deb(message: String) = logDebug(logger, message)
fun CreateCommentSender.deb(message: String) = logDebug(logger, message)
fun PullRequestsOfCommitReceiver.deb(message: String) = logDebug(logger, message)
fun RequestChangesSender.deb(message: String) = logDebug(logger, message)
fun RemoveRequestChangesSender.deb(message: String) = logDebug(logger, message)
fun RemoveCommentSender.deb(message: String) = logDebug(logger, message)
fun CommentsPayload.deb(message: String) = logDebug(logger, message)
fun CommentsReceiver.deb(message: String) = logDebug(logger, message)
fun CommentsNextPagePayload.deb(message: String) = logDebug(logger, message)
fun CommentsPaginatedPayload.deb(message: String) = logDebug(logger, message)
fun DiffReceiver.deb(message: String) = logDebug(logger, message)
fun PMDAnalyzer.deb(message: String) = logDebug(logger, message)
fun PMDViolationsCollector.deb(message: String) = logDebug(logger, message)

private fun logDebug(logger: Logger, message: String) = if (debug()) logger.debug(message) else Unit

fun httpClient(): CloseableHttpClient = HttpClientBuilder.create().build()
