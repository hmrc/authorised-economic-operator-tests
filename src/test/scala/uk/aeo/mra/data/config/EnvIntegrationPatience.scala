package uk.aeo.mra.data.config

import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.time._

trait EnvIntegrationPatience extends IntegrationPatience with Eventually {

  implicit abstract override val patienceConfig: PatienceConfig = {
    val (envBasedTimeout, envBasedInterval) = System.getProperty("env") match {
      case _ => (scaled(Span(10, Seconds)), scaled(Span(10, Seconds)))
      case "development" => (scaled(Span(10, Seconds)), scaled(Span(10, Seconds)))
      case "qa" => (scaled(Span(10, Seconds)), scaled(Span(10, Seconds)))

    }
    PatienceConfig(timeout = envBasedTimeout, interval = envBasedInterval)
  }
}
