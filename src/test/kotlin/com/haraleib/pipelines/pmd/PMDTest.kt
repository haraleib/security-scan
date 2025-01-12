package com.haraleib.pipelines.pmd

import com.haraleib.pipelines.diff.LineChange
import com.haraleib.pipelines.report.ReportSettings
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.text.Charsets.UTF_8

class PMDTest {

  @TempDir lateinit var folder: Path
  private lateinit var settings: ReportSettings
  private val javaFileName = "ASimpleJavaClass.java"
  private val javaFileContent = """
      package com.haraleib.pipelines.pmd.testfiles;

      public class ASimpleJavaClass {

        public void test(){
          String t = "Hello World!";
          System.out.println("Hello world!");
        }
      }
    """.trimIndent().toByteArray(UTF_8)

  @BeforeEach
  fun init() {
    val workDir = folder.resolve("work_dir")
    val resultDir = folder.resolve("result_dir")

    workDir.toFile().mkdir()
    resultDir.toFile().mkdir()

    settings = ReportSettings(
      workdir = workDir,
      reportOutputPath = resultDir,
      reportFormat = "xml"
    )

    val javaFilePath = settings.workdir.resolve(javaFileName)
    Files.write(javaFilePath, javaFileContent)
  }

  @Test
  fun should_collect_all_violations_of_java_class() {
    //arrange
    PMDAnalyzer(settings, listOf(javaFileName)).analyze()

    //act
    val result = PMDViolationsCollector(settings, listOf()).collect()

    //assert
    assertEquals(6, result.size)
  }

  @Test
  fun should_find_two_violations_in_line_change() {
    //arrange
    val changedLine = LineChange(6, "String t = \"Hello World!\";", "${settings.workdir}/ASimpleJavaClass.java")
    PMDAnalyzer(settings, listOf(javaFileName)).analyze()

    //act
    val result = PMDViolationsCollector(settings, listOf(changedLine)).collect()

    //assert
    val iter = result.iterator()
    assertEquals(2, result.size)
    assertTrue(iter.next().message.startsWith("Avoid unused local variables such as 't'"))
    assertTrue(iter.next().message.startsWith("Avoid variables with short names like t"))
  }
}
