package com.haraleib.pipelines.api

import org.apache.commons.text.StringEscapeUtils

data class SecurityScanViolation(private val message: String, val startLine: Int, val fileName: String) {

  val sanitizedMessage: String = StringEscapeUtils.escapeHtml4(message.replace("\n", "\\n"))
}
