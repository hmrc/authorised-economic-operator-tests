package uk.aeo.mra.data.config

import org.scalatest.Assertions
import uk.aeo.mra.data.page.Port
import uk.gov.hmrc.endtoend.sa.{AbsoluteUrl, RelativeUrl}

trait EndToEndTestConfig extends Assertions {

  implicit def absoluteUrl[Y <: RelativeUrl with Port](implicit config: TestConfig): AbsoluteUrl[Y] = new AbsoluteUrl[Y] {
    def apply(p: Y): String =
      if (testConfig.requiresPort)
        s"${config.apiProxyUrl}:${p.port}/${p.relativeUrl}"
      else
        s"${config.apiProxyUrl}/${p.relativeUrl}"
  }

  implicit lazy val testConfig: TestConfig = System.getProperty("env") match {
    case null | "local" =>
      TestConfig(
        requiresPort = true,
        apiProxyUrl = "http://localhost:9937",
        url = "http://localhost:9939")
    case "development" =>
      TestConfig(
        requiresPort = true,
        apiProxyUrl = "http://localhost:9937",
        url = "http://localhost:9939")
    case "qa" =>
      TestConfig(
        requiresPort = false,
        apiProxyUrl = "https://authorised-economic-operator-data-converter.protected.mdtp",
        url = "https://authorised-economic-operator-stubs.protected.mdtp")
    case "staging" =>
      TestConfig(
        requiresPort = false,
        apiProxyUrl = "https://authorised-economic-operator-data-converter.protected.mdtp",
        url = "https://authorised-economic-operator-stubs.protected.mdtp")
    case other => fail(s"Environment '$other' not recognised")
  }
}

object EndToEndTestConfig extends EndToEndTestConfig