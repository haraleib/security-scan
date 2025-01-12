package com.haraleib.pipelines.approvals

import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.api.Sender
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI

class RequestChangesSender(private val settings: BitbucketSettings) : Sender<Payload> {

  val logger: Logger = getLogger(RequestChangesSender::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${settings.workSpace}/${settings.repoSlug}/pullrequests/${settings.pullRequestId}/request-changes")

  override fun send(payload: Payload): StatusLine =
    httpClient().use {
      deb("Request changes for PR.\nURI is: $uri")

      val post = HttpPost(uri)
      post.setHeader(ACCEPT, APPLICATION_JSON.mimeType)
      post.setHeader(AUTHORIZATION, "Bearer ${settings.repoAccessToken}")

      val response = it.execute(post)
      response.statusLine
  }
}
