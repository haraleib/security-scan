package com.haraleib.pipelines.api

import org.apache.http.StatusLine

interface Sender<T : Payload> {

  fun send(payload: T) : StatusLine

}
