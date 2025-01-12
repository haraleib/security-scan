package com.haraleib.pipelines.create

import com.haraleib.pipelines.api.Sender
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.apache.http.entity.StringEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI

class CreateCommentSender(private val settings: BitbucketSettings) : Sender<CreateCommentPayload> {

  val logger: Logger = getLogger(CreateCommentSender::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${settings.workSpace}/${settings.repoSlug}/pullrequests/${settings.pullRequestId}/comments")

  override fun send(payload: CreateCommentPayload): StatusLine =
    httpClient().use {
      val comment = payload.build()
      deb("URI $uri \n Sending new comment $comment \n")

      val post = HttpPost(uri)
      post.entity = StringEntity(comment, APPLICATION_JSON)
      post.setHeader(ACCEPT, APPLICATION_JSON.mimeType)
      post.setHeader(AUTHORIZATION, "Bearer ${settings.repoAccessToken}")

      val response = it.execute(post)
      response.statusLine
  }

}
