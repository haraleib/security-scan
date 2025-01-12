package com.haraleib.pipelines.comments

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CommentsPayloadTest {

  @Test
  fun empty_result_if_no_url_given(){
    //arrange
    val payload = CommentsPayload("""
        {
          "values": [],
          "pagelen": 10,
          "size": 0,
          "page": 1
        }
    """.trimIndent())

    val sut = CommentsNextPagePayload(payload.build())

    //act
    val nextUrl = sut.build()

    //assert
    Assertions.assertTrue { nextUrl.isEmpty() }
  }

  @Test
  fun extract_next_comments_page_url(){
    //arrange
    val url = "https://api.bitbucket.org/2.0/repositories/haraldeibensteiner/simple-java-maven-app/pullrequests/27/comments"
    val payload = CommentsPayload("""
        {
          "values": [],
          "pagelen": 10,
          "size": 0,
          "page": 1,
          "next": "$url"
        }
    """.trimIndent())

    val sut = CommentsNextPagePayload(payload.build())

    //act
    val nextUrl = sut.build()

    //assert
    assertEquals(url, nextUrl)
  }


  @Test
  fun no_comments_available(){
    //arrange
    val payload = CommentsPayload("""
        {
          "values": [],
          "pagelen": 10,
          "size": 0,
          "page": 1
        }
    """.trimIndent())

    val sut = CommentsPaginatedPayload(listOf(payload))

    //act
    val commentIds = sut.extractCommentIds()

    //assert
    assertEquals(0, commentIds.size)
  }

  @Test
  fun retrieve_pull_request_id_of_payload(){
    //arrange
    val payload = CommentsPayload(
     """
          {
            "values": [
              {
                "id": 573568542,
                "created_on": "2024-12-04T13:24:24.297625+00:00",
                "updated_on": "2024-12-04T13:24:24.297708+00:00",
                "content": {
                  "type": "rendered",
                  "raw": "test",
                  "markup": "markdown",
                  "html": "<p>test</p>"
                },
                "user": {
                  "display_name": "security-scan",
                  "links": {
                    "self": {
                      "href": "https://api.bitbucket.org/2.0/users/{d1859150-30db-456d-96b3-ce14a8299294}"
                    },
                    "avatar": {
                      "href": "https://secure.gravatar.com/avatar/c9a526295b971417da9a51e57fca08bb?d=https://avatar-management--avatars.us-west-2.prod.public.atl-paas.net/initials/HE-1.png"
                    },
                    "html": {
                      "href": "https://bitbucket.org/{d1859150-30db-456d-96b3-ce14a8299294}/"
                    }
                  },
                  "type": "user",
                  "uuid": "{d1859150-30db-456d-96b3-ce14a8299294}",
                  "account_id": "557058:ef067572-4cd2-4616-80f6-596c87ab9298",
                  "nickname": "security-scan"
                },
                "deleted": false,
                "pending": false,
                "type": "pullrequest_comment",
                "links": {
                  "self": {
                    "href": "https://api.bitbucket.org/2.0/repositories/haraldeibensteiner/simple-java-maven-app/pullrequests/4/comments/573568542"
                  },
                  "html": {
                    "href": "https://bitbucket.org/haraldeibensteiner/simple-java-maven-app/pull-requests/4/_/diff#comment-573568542"
                  }
                },
                "pullrequest": {
                  "type": "pullrequest",
                  "id": 4,
                  "title": "a commit",
                  "links": {
                    "self": {
                      "href": "https://api.bitbucket.org/2.0/repositories/haraldeibensteiner/simple-java-maven-app/pullrequests/4"
                    },
                    "html": {
                      "href": "https://bitbucket.org/haraldeibensteiner/simple-java-maven-app/pull-requests/4"
                    }
                  }
                }
              },
              {
                "id": 573568564,
                "created_on": "2024-12-04T13:24:26.785285+00:00",
                "updated_on": "2024-12-04T13:24:26.785390+00:00",
                "content": {
                  "type": "rendered",
                  "raw": "test",
                  "markup": "markdown",
                  "html": "<p>test</p>"
                },
                "user": {
                  "display_name": "Harald Eibensteiner",
                  "links": {
                    "self": {
                      "href": "https://api.bitbucket.org/2.0/users/{d1859150-30db-456d-96b3-ce14a8299294}"
                    },
                    "avatar": {
                      "href": "https://secure.gravatar.com/avatar/c9a526295b971417da9a51e57fca08bb?d=https://avatar-management--avatars.us-west-2.prod.public.atl-paas.net/initials/HE-1.png"
                    },
                    "html": {
                      "href": "https://bitbucket.org/{d1859150-30db-456d-96b3-ce14a8299294}/"
                    }
                  },
                  "type": "user",
                  "uuid": "{d1859150-30db-456d-96b3-ce14a8299294}",
                  "account_id": "557058:ef067572-4cd2-4616-80f6-596c87ab9298",
                  "nickname": "Harald Eibensteiner"
                },
                "deleted": false,
                "pending": false,
                "type": "pullrequest_comment",
                "links": {
                  "self": {
                    "href": "https://api.bitbucket.org/2.0/repositories/haraldeibensteiner/simple-java-maven-app/pullrequests/4/comments/573568564"
                  },
                  "html": {
                    "href": "https://bitbucket.org/haraldeibensteiner/simple-java-maven-app/pull-requests/4/_/diff#comment-573568564"
                  }
                },
                "pullrequest": {
                  "type": "pullrequest",
                  "id": 4,
                  "title": "a commit",
                  "links": {
                    "self": {
                      "href": "https://api.bitbucket.org/2.0/repositories/haraldeibensteiner/simple-java-maven-app/pullrequests/4"
                    },
                    "html": {
                      "href": "https://bitbucket.org/haraldeibensteiner/simple-java-maven-app/pull-requests/4"
                    }
                  }
                }
              }
            ],
            "pagelen": 10,
            "size": 2,
            "page": 1
          }
     """.trimIndent()
    )
    val sut = CommentsPaginatedPayload(listOf(payload))

    //act
    val commentIds = sut.extractCommentIds()

    //Assert
    assertEquals(1, commentIds.size)
    assertEquals("573568542", commentIds.first())
  }
}
