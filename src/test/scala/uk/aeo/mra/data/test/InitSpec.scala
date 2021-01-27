package uk.aeo.mra.data.test

import org.scalatest.{BeforeAndAfterEach, Outcome}
import uk.aeo.mra.data.api.WithWsClient
import uk.aeo.mra.data.config.{EndToEndTestConfig, EnvIntegrationPatience}
import uk.gov.hmrc.endtoend.sa.Spec

import scala.util.Try

object PlatformWarmer {
  private val preWarmer = new PreWarmer

  def main(args: Array[String]) = {
    println("Starting the platform warm up")
    preWarmer.warmUpThePlatform()
    preWarmer.wsClient.close()
    Try(preWarmer.webDriver.quit())
    println("Platform warm up finished")
  }
}

class PreWarmer extends Spec with WithCleanDBs with WithWsClient with EnvIntegrationPatience {

  def warmUpThePlatform() = {
    try {
      eventually {
        cleanup
      }
    } catch {
      case e: Throwable => {}
    }
  }
}


class AcceptanceTestSpec extends Spec with WithCleanDBs with WithWsClient with EnvIntegrationPatience {

  override def afterAll(): Unit = {
    super.afterAll()
    wsClient.close()
  }

  override def withFixture(test: NoArgTest): Outcome = {
    val fixture = test()
    fixture
  }
}

trait WithCleanDBs extends Spec with WithWsClient with BeforeAndAfterEach with EndToEndTestConfig {

  override def beforeEach(): Unit = {
    cleanup
  }

  def cleanup = {
    println("[info] Deleting all data in DBs")

  }
}