package com.haraleib.pipelines.create

import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.api.SecurityScanViolation

class CreateCommentPayload(private val violation: SecurityScanViolation) : Payload {

  override fun build() =
    """
      {
        "content": {
           "raw": "${violation.sanitizedMessage}"
        },
        "inline": {
          "to": ${violation.startLine},
          "path": "${violation.fileName}"
        }
     }
    """.trimIndent()
}
