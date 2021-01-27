package uk.aeo.mra.data.api

import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.ws.WSResponse
import uk.aeo.mra.data.config.{EndToEndTestConfig, EnvIntegrationPatience}

import scala.concurrent.Future

trait Api
  extends ScalaFutures
    with EnvIntegrationPatience
    with Matchers
    with EndToEndTestConfig
    with WithWsClient
    with EnvRestrictions {

  def serviceName: String

  def proxyRequests: Boolean = true

  def GET(path: String): WSResponse = {
    val url = path
    safeExecution("GET", url) {
      wsClient.url(url).get()
    }
  }

  def POST[T](path: String, body: T)(implicit wrt: play.api.http.Writeable[T],
                                     ct: play.api.http.ContentTypeOf[T]): WSResponse = {
    val url = absoluteUrl(path)
    safeExecution("POST", url) {
      retryIfLocked {
        wsClient.url(url).post(body)
      }
    }
  }

  def PUT[T](path: String, body: T)(implicit wrt: play.api.http.Writeable[T],
                                    ct: play.api.http.ContentTypeOf[T]): WSResponse = {
    val url = absoluteUrl(path)
    safeExecution("PUT", url) {
      retryIfLocked {
        wsClient.url(url).put(body)
      }
    }
  }

  def DELETE(path: String): WSResponse = {
    val url = absoluteUrl(path)
    safeExecution("DELETE", url) {
      wsClient.url(url).delete()
    }
  }

  private def absoluteUrl(url: String) = {
    if (proxyRequests)
      s"${testConfig.apiProxyUrl}/$url"
    else
      url
  }

  def retryIfLocked(body: => Future[WSResponse]): Future[WSResponse] = {
    body.futureValue match {
      case r if r.status == 423 => retryIfLocked(body)
      case other => Future.successful(other)
    }
  }

  def safeExecution(method: String, url: String)(body: => Future[WSResponse]): WSResponse = {
    if (isAllowed) {
      val response = body.futureValue
      println(s"[info] ${method.padTo(7, " ").mkString} to $url returned with status: ${response.status} and body: '${response.body.take(500)}'")
      response
    } else throw new RuntimeException(s"[error] $method to $url prevented because of environment restrictions")
  }
}

trait WithWsClient {
  val wsClient = {
    val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
    new play.api.libs.ws.ning.NingWSClient(builder.build())
  }
}

trait EnvRestrictions {
  def isAllowed: Boolean = true
}

trait LocalOnly extends EnvRestrictions {
  override def isAllowed: Boolean = true
}
