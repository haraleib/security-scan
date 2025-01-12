package com.haraleib.pipelines.create

import com.haraleib.pipelines.api.SecurityScanViolation
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CreateCommentPayloadTest {

  @Test
  fun should_be_able_to_create_comment_payload() {
    val violation = SecurityScanViolation("This is my message", 3, "/a/file/path")
    val payload = CreateCommentPayload(violation).build()
    assertTrue(payload.isNotEmpty())
  }
}
