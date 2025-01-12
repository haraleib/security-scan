package com.haraleib.pipelines.api

fun interface ViolationsCollector<SecurityScanViolation> {

  fun collect(): Set<SecurityScanViolation>

}
