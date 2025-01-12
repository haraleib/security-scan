package com.haraleib.pipelines.create

import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.api.SecurityScanViolation

class CreateCommentPayload(private val violation: SecurityScanViolation) : Payload {

  override fun build() =
    """
      {
        "content": {
           "raw": "${violation.message.replace("\n", "\\n")}"
        },
        "inline": {
          "to": ${violation.startLine},
          "path": "${violation.fileName}"
        }
     }
    """.trimIndent()

/*
  """
      {
        "content": {
           "raw": "${violation.message.replace("\n", "\\n")}"
        },
        "inline": {
          it seems that when using it this way the comment is made in from / startLine in the old line not in the new line diff
          "from": ${violation.startLine},
          "to": ${violation.endLine},
          "path": "${violation.sanitizedFile()}"
        }
     }
    """.trimIndent()*/
}
