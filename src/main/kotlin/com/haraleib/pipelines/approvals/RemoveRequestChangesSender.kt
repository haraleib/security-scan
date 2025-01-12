package com.haraleib.pipelines.approvals

import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.api.Sender
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpDelete
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI

class RemoveRequestChangesSender(private val settings: BitbucketSettings) : Sender<Payload> {

  val logger: Logger = getLogger(RemoveRequestChangesSender::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${settings.workSpace}/${settings.repoSlug}/pullrequests/${settings.pullRequestId}/approve")

  override fun send(payload: Payload): StatusLine =
    httpClient().use {
      deb("Approving PR.\nURI is: $uri")

      val post = HttpDelete(uri)
      post.setHeader(AUTHORIZATION, "Bearer ${settings.repoAccessToken}")

      val response = it.execute(post)
      response.statusLine
    }
}
