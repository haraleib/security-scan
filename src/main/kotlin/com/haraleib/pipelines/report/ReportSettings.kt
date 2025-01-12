package com.haraleib.pipelines.report

import java.nio.file.Path

data class ReportSettings(
  val workdir: Path,
  val reportOutputPath: Path,
  val reportFormat: String,
  val reportFileName: String = "pmd-report-result.$reportFormat",
  val fullReportFilename : Path = reportOutputPath.resolve(reportFileName)
  )
