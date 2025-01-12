package com.haraleib.pipelines.comments

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.util.deb
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

class CommentsNextPagePayload(private val payload : String) : Payload {
  val logger: Logger = getLogger(CommentsNextPagePayload::class.java)

  override fun build(): String {
    return extractNextPageUrl()
  }

  private fun extractNextPageUrl(): String {
    val jsonStr = payload
    val mapper = ObjectMapper()
    val jsonNode = mapper.readValue(jsonStr, JsonNode::class.java)
    val nextPageUrl = jsonNode.get("next")?.asText("") ?: ""
    deb("Next page is $nextPageUrl")

    return nextPageUrl
  }
}
