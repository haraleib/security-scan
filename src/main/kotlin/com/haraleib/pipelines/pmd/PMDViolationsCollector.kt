package com.haraleib.pipelines.pmd

import com.haraleib.pipelines.api.SecurityScanViolation
import com.haraleib.pipelines.api.ViolationsCollector
import com.haraleib.pipelines.diff.LineChange
import com.haraleib.pipelines.report.ReportSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import se.bjurr.violations.lib.ViolationsApi
import se.bjurr.violations.lib.model.Violation
import se.bjurr.violations.lib.reports.Parser
import java.nio.file.Path

class PMDViolationsCollector(private val settings : ReportSettings, private val changedLines : List<LineChange>) : ViolationsCollector<SecurityScanViolation> {

  val logger: Logger = getLogger(PMDViolationsCollector::class.java)

  override fun collect(): Set<SecurityScanViolation> {
    val changedLinesNumbers = changedLines.map { it.lineNumber }.toSet()
    val performOnFullClass = changedLinesNumbers.isEmpty()
    return ViolationsApi.violationsApi()
      .withPattern(".*/${settings.reportFileName}$")
      .inFolder(settings.reportOutputPath.toString())
      .findAll(Parser.PMD)
      .violations()
      .filter { performOnFullClass || changedLinesNumbers.contains(it.startLine) }
      .map { SecurityScanViolation(it.message, it.startLine, sanitizeFileName(it, settings.workdir)) }
      .sortedBy { it.startLine }
      .toSet()
  }

  private fun sanitizeFileName(violation : Violation, workdir : Path) : String {
    val cloneDir = "${workdir}/".replace("\\", "/")
    return violation.file.removePrefix(cloneDir)
  }
}

