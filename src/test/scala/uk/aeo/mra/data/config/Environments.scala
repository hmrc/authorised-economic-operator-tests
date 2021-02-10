
package uk.aeo.mra.data.config

import com.typesafe.config.ConfigFactory

trait Environments {

  private val config = ConfigFactory.load("application.conf")
  val env: String = Option(System.getProperty("env")).getOrElse("local")

  val aeoRepo: String = config.getString(s"environments.$env.aeo")

  val aeoStubs: String = config.getString(s"environments.$env.aeo-stubs")

}

