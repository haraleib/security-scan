package com.haraleib.pipelines.pmd

import com.haraleib.pipelines.api.Analyzer
import com.haraleib.pipelines.report.ReportSettings
import com.haraleib.pipelines.util.EnvVars.debug
import com.haraleib.pipelines.util.deb
import net.sourceforge.pmd.PMDConfiguration
import net.sourceforge.pmd.PmdAnalysis
import net.sourceforge.pmd.lang.java.JavaLanguageModule
import net.sourceforge.pmd.renderers.RendererFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.FileWriter
import java.util.*

class PMDAnalyzer(private val settings : ReportSettings, private val changedFiles : List<String>) : Analyzer {

  val logger: Logger = getLogger(PMDAnalyzer::class.java)

  override fun analyze() {
    val config = PMDConfiguration();
    config.setDefaultLanguageVersion(JavaLanguageModule.getInstance().defaultVersion)
    config.inputPathList = changedFiles.map { settings.workdir.resolve(it)  }.toList()

    for(inputPath in config.inputPathList) {
      deb(inputPath.toString())
    }

    PmdAnalysis.create(config).use { pmd ->
      if (debug()) {
        addConsoleRenderer(pmd)
      }
      addFileRenderer(pmd)
      pmd.newRuleSetLoader().standardRuleSets.forEach { ruleSet -> pmd.addRuleSet(ruleSet) }
      pmd.performAnalysis()
    }
  }

  private fun addConsoleRenderer(pmd : PmdAnalysis){
/*    val consoleRenderer = RendererFactory.createRenderer("textcolor", Properties())
    consoleRenderer.writer = System.out.writer()
    pmd.addRenderer(consoleRenderer)*/
  }

  private fun addFileRenderer(pmd : PmdAnalysis){
    val renderer = RendererFactory.createRenderer(settings.reportFormat, Properties())
    val fileWriter = FileWriter(settings.fullReportFilename.toFile())
    renderer.writer = fileWriter
    pmd.addRenderer(renderer)
  }

}
