package uk.aeo.mra.data.config

import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.time._

trait EnvIntegrationPatience extends IntegrationPatience with Eventually {

  implicit abstract override val patienceConfig: PatienceConfig = {
    val (envBasedTimeout, envBasedInterval) = System.getProperty("env") match {
//      case "dev" => (scaled(Span(5, Minutes)), scaled(Span(1, Minute)))
      case _ => (scaled(Span(15, Seconds)), scaled(Span(150, Milliseconds)))
    }
    PatienceConfig(timeout = envBasedTimeout, interval = envBasedInterval)
  }
}
