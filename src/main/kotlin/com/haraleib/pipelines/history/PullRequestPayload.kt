package com.haraleib.pipelines.history

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.haraleib.pipelines.api.Payload

class PullRequestPayload(private val payload : String) : Payload {

  override fun build(): String {
    val mapper = ObjectMapper()
    val jsonNode =  mapper.readValue(payload, JsonNode::class.java)
    return jsonNode.get("values")[0].get("id").asText()
  }

}
