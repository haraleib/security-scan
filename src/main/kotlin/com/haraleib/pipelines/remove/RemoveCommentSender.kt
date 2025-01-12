package com.haraleib.pipelines.remove

import com.haraleib.pipelines.api.Payload
import com.haraleib.pipelines.api.Sender
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpDelete
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI

class RemoveCommentSender(private val bitbucket: BitbucketSettings, private val commentId : String) : Sender<Payload> {

  val logger: Logger = getLogger(RemoveCommentSender::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${bitbucket.workSpace}/${bitbucket.repoSlug}/pullrequests/${bitbucket.pullRequestId}/comments/$commentId")

  override fun send(payload: Payload): StatusLine =
    httpClient().use {
      deb("Trying to remove comment with id $commentId.\nURI is $uri")

      val delete = HttpDelete(uri)
      delete.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${bitbucket.repoAccessToken}")

      val response = it.execute(delete)
      response.statusLine
  }
}
