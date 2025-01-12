package com.haraleib.pipelines.diff

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class DiffExtractorTest {

  private val payload = DiffPayload("""
                    diff --git a/src/main/java/com/mycompany/app/App.java b/src/main/java/com/mycompany/app/App.java
                    index 2b6443a..6390e6e 100644
                    --- a/src/main/java/com/mycompany/app/App.java
                    +++ b/src/main/java/com/mycompany/app/App.java
                    @@ -8,18 +8,21 @@ public class App {
                         public App() {}
                     
                         public static void main(String[] args) {
                    -        if (false) {
                    +    
                    +        System.out.printLine("This is a change");
                    +    
                    +               if (true) {
                     
                    -        } else if (true) {
                    +                      } if (false) {
                     
                             }
                    -        if (false) {
                     
                    -        } else if (true) {
                    -
                    -        }
                    +        System.out.println("asdf");
                    +    }
                     
                    -        System.out.println(MESSAGE);
                    +    public String getTest(){
                    +    String test ="asdf";
                    +    return test;
                         }
                     
                         public String getMessage() {
               """.trimIndent())

  @Test
  fun extract_changed_lines_from_diff() {
    //act
    val result = DiffExtractor.extractChangedLines(payload)

    //act / assert
    assertEquals(11, result[0].lineNumber)
    assertEquals(12, result[1].lineNumber)
    assertEquals(13, result[2].lineNumber)
    assertEquals(14, result[3].lineNumber)
    assertEquals(16, result[4].lineNumber)
    assertEquals(20, result[5].lineNumber)
    assertEquals(21, result[6].lineNumber)
    assertEquals(23, result[7].lineNumber)
    assertEquals(24, result[8].lineNumber)
    assertEquals(25, result[9].lineNumber)
    assertTrue(result.map { it.filePath }.all { it == "src/main/java/com/mycompany/app/App.java"})
  }
}
