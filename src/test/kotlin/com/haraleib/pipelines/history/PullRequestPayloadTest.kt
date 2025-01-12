package com.haraleib.pipelines.history

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class PullRequestPayloadTest {

  @Test
  fun retrieve_pull_request_id_of_payload(){
    //arrange
    val sut = PullRequestPayload(
      """
      {
        "type": "paginated_pullrequests",
        "values": [
          {
            "type": "pullrequest",
            "id": 1,
            "title": "TEST PR",
            "links": {
              "self": {
                "href": "https://api.bitbucket.org/2.0/repositories/testworkspace/testrepo/pullrequests/1"
              },
              "html": {
                "href": "https://bitbucket.org/repositories/testworkspace/testrepo/pull-requests/1"
              }
            }
          }
        ],
        "page": 1,
        "pagelen": 50
      }
    """.trimIndent()
    )

    //act
    val prId = sut.build()

    //Assert
    Assertions.assertEquals("1", prId)
  }
}
