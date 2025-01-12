package com.haraleib.pipelines.comments

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.util.deb
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

class CommentsPaginatedPayload(private val payloads : List<CommentsPayload>) : Payload {

  val logger: Logger = getLogger(CommentsPaginatedPayload::class.java)
  private val paginatedCommentsDelimiter = "{NEXT PAGE}";

  override fun build(): String {
    return payloads.joinToString(paginatedCommentsDelimiter) { it.build() }
  }

  fun extractCommentIds(): MutableList<String> {
    val jsonStr = build()
    val commentIds = mutableListOf<String>()
    val pages = jsonStr.split(paginatedCommentsDelimiter)

    for(page in pages) {
      commentIds.addAll(extractCommentIdsPerPage(page))
    }

    deb("The following commentIds where found: $commentIds")

    return commentIds
  }

  private fun extractCommentIdsPerPage(jsonStr : String) : MutableList<String> {
    val mapper = ObjectMapper()
    val jsonNode = mapper.readValue(jsonStr, JsonNode::class.java)
    val arrayNode = jsonNode.get("values") as ArrayNode
    val commentIds = mutableListOf<String>()
    for(node in arrayNode){
      val user = node.get("user")
      if(user.get("display_name").asText() == "security-scan"){
        commentIds.add(node.get("id").asText())
      }
    }
    return commentIds
  }
}
