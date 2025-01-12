package com.haraleib.pipelines.diff

object DiffExtractor {

  fun extractChangedLines(payload: DiffPayload): List<LineChange> {
    return extractChangedLines(payload.build())
          .sortedBy { it.lineNumber }
  }

  private fun extractChangedLines(diff: String): List<LineChange> {
    val addedLines = mutableListOf<LineChange>()
    var currentNewLine = 0
    var currentFileName = ""
    var inHunk = false

    diff.lines().forEach { line ->
      when {
        isFileNameLine(line) -> currentFileName = line.split(" ")[1].substring(2)

        isHunk(line) -> {
          inHunk = true
          val hunkParts = line.split(" ")
          val newFileInfo = hunkParts[2]
          currentNewLine = newFileInfo.split(",")[0].substring(1).toInt()
        }

        isAddedLine(inHunk, line) -> {
          addedLines.add(LineChange(currentNewLine, line.substring(1).trim(), currentFileName))
          currentNewLine++
        }

        isNotModifiedLine(inHunk, line) -> currentNewLine++
      }
    }
    return addedLines
  }

  private fun isFileNameLine(line: String) = line.startsWith("+++ b")

  private fun isHunk(line: String) = line.startsWith("@@")

  private fun isAddedLine(inHunk: Boolean, line: String) = inHunk && line.startsWith("+") && !line.startsWith("+++")

  private fun isNotModifiedLine(inHunk: Boolean, line: String) = inHunk && !line.startsWith("-")
}
