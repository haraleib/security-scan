package com.haraleib.pipelines.bitbucket

data class BitbucketSettings(
    val workSpace: String,
    val repoSlug: String,
    val pullRequestId: String,
    val commit: String,
    val repoAccessToken: String
)
