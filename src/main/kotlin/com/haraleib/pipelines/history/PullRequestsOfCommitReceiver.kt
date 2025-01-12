package com.haraleib.pipelines.history

import com.haraleib.pipelines.api.Receiver
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


class PullRequestsOfCommitReceiver (workspace: String, repoSlug: String, private val commit : String, private val repoAccessToken: String) : Receiver<PullRequestPayload> {

  val logger: Logger = getLogger(PullRequestsOfCommitReceiver::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/$workspace/$repoSlug/commit/$commit/pullrequests")

  override fun receive(): PullRequestPayload =
    httpClient().use { client ->
      deb("URI $uri \n Trying to receive current pull requests of commit $commit")

      val get = HttpGet(uri)
      get.setHeader(ACCEPT, APPLICATION_JSON.mimeType)
      get.setHeader(AUTHORIZATION, "Bearer $repoAccessToken")

      val response = client.execute(get)
      val result = EntityUtils.toString(response.entity)
      PullRequestPayload(result)
  }
}

