package com.haraleib.pipelines.comments

import com.haraleib.pipelines.api.Payload
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

class CommentsPayload(private val payload : String) : Payload {
  val logger: Logger = getLogger(CommentsReceiver::class.java)

  override fun build(): String {
    return payload
  }
}
