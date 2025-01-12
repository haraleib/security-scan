package com.haraleib.pipelines.diff

import com.haraleib.pipelines.api.Payload

class DiffPayload(private val payload: String) : Payload {

  override fun build() = payload

}
