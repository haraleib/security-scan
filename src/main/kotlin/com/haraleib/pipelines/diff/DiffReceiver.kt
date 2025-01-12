package com.haraleib.pipelines.diff

import com.haraleib.pipelines.api.Receiver
import com.haraleib.pipelines.bitbucket.BitbucketSettings
import com.haraleib.pipelines.util.deb
import com.haraleib.pipelines.util.httpClient
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.net.URI

class DiffReceiver (private val bitbucket: BitbucketSettings) : Receiver<DiffPayload> {

  val logger: Logger = getLogger(DiffReceiver::class.java)
  private val uri: URI = URI.create("https://api.bitbucket.org/2.0/repositories/${bitbucket.workSpace}/${bitbucket.repoSlug}/pullrequests/${bitbucket.pullRequestId}/diff")

  override fun receive(): DiffPayload =
    httpClient().use { client ->
      deb("Trying to receive current diff.\nURI is $uri")

      val get = HttpGet(uri)
      get.setHeader(AUTHORIZATION, "Bearer ${bitbucket.repoAccessToken}")

      val response = client.execute(get)
      val result = EntityUtils.toString(response.entity)
      DiffPayload(result)
  }
}
