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
        apiProxyUrl = "http://localhost:8234",
        url = "http://localhost:9000")
    case "dev" =>
      TestConfig(
        requiresPort = false,
        url = "https://authorised-economic-operator-stubs.protected.mdtp",
        apiProxyUrl = "https://authorised-economic-operator-data-converter.protected.mdtp")
    case "qa" =>
      TestConfig(
        requiresPort = false,
        url = "https://authorised-economic-operator-stubs.protected.mdtp",
        apiProxyUrl = "https://authorised-economic-operator-data-converter.protected.mdtp")
    case "staging" =>
      TestConfig(
        requiresPort = false,
        url = "https://authorised-economic-operator-stubs.protected.mdtp",
        apiProxyUrl = "https://authorised-economic-operator-data-converter.protected.mdtp")
    case other => fail(s"Environment '$other' not recognised")
  }
}

object EndToEndTestConfig extends EndToEndTestConfig