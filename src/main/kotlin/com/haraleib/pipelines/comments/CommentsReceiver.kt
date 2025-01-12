package com.haraleib.pipelines.comments

import com.haraleib.pipelines.api.Receiver
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI


class CommentsReceiver (private val bitbucket: BitbucketSettings) : Receiver<CommentsPaginatedPayload> {

  val logger: Logger = getLogger(CommentsReceiver::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${bitbucket.workSpace}/${bitbucket.repoSlug}/pullrequests/${bitbucket.pullRequestId}/comments")

  override fun receive(): CommentsPaginatedPayload {
    val commentsPayloads = mutableListOf<CommentsPayload>()
    var uri: URI? = uri

    do {
      val result = receive(uri)
      commentsPayloads.add(CommentsPayload(result))
      uri = toURI(result)
    } while (uri != null)

    return CommentsPaginatedPayload(commentsPayloads)
  }

  private fun receive(uri : URI?) : String =
    httpClient().use { client ->
      deb("Trying to receive current comments of pull request.\nURI is $uri")

      val get = HttpGet(uri)
      get.setHeader(ACCEPT, APPLICATION_JSON.mimeType)
      get.setHeader(AUTHORIZATION, "Bearer ${bitbucket.repoAccessToken}")

      val response = client.execute(get)
      EntityUtils.toString(response.entity)
  }

  private fun toURI(result: String): URI? {
    val nextUri = CommentsNextPagePayload(result).build()
    return nextUri.ifEmpty { null }?.let { URI.create(it) }
  }
}

