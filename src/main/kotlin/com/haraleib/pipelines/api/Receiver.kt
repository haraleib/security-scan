package com.haraleib.pipelines.api

interface Receiver<T : Payload> {

  fun receive(): T

}
