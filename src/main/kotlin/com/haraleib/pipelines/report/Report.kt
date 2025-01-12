package com.haraleib.pipelines.report

import com.haraleib.pipelines.diff.LineChange
import com.haraleib.pipelines.pmd.PMDAnalyzer
import com.haraleib.pipelines.pmd.PMDViolationsCollector

class Report(private val settings: ReportSettings) {

  fun analyze(filePaths : List<String>) = PMDAnalyzer(settings, filePaths).analyze()

  fun collectValidations(changedLines : List<LineChange>) = PMDViolationsCollector(settings, changedLines).collect()

}
