package com.haraleib.pipelines.bitbucket

import com.haraleib.pipelines.approvals.RemoveRequestChangesSender
import com.haraleib.pipelines.approvals.RequestChangesSender
import com.haraleib.pipelines.comments.CommentsReceiver
import com.haraleib.pipelines.create.CreateCommentPayload
import com.haraleib.pipelines.create.CreateCommentSender
import com.haraleib.pipelines.diff.DiffReceiver
import com.haraleib.pipelines.remove.RemoveCommentSender
import com.haraleib.pipelines.util.err
import com.haraleib.pipelines.util.inf
import org.apache.http.StatusLine
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

class Bitbucket(private val settings: BitbucketSettings) {

  val logger: Logger = getLogger(Bitbucket::class.java)

  fun createComment(payload: CreateCommentPayload) = contOnError { CreateCommentSender(settings).send(payload) }
  fun removeComment(commentId: String) = contOnError { RemoveCommentSender(settings, commentId).send { "" } }
  fun requestChanges() = contOnError { RequestChangesSender(settings).send { "" } }
  fun removeRequestChanges() = contOnError { RemoveRequestChangesSender(settings).send { "" } }

  fun receiveDiff() = failOnError { DiffReceiver(settings).receive() }
  fun receiveComments() = failOnError { CommentsReceiver(settings).receive() }

  private fun contOnError(supplier: () -> StatusLine) {
    try {
      val statusLine = supplier()
      inf("Response: ${statusLine.statusCode}  ${statusLine.reasonPhrase}")
    } catch (t: Throwable) {
      err("Error on requesting bitbucket rest api: $t")
    }
  }

  private fun <T> failOnError(supplier: () -> T): T {
    try {
      return supplier.invoke()
    } catch (t: Throwable) {
      err("Error on requesting bitbucket rest api: $t")
      throw t
    }
  }
}

