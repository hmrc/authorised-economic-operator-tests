package uk.aeo.mra.data.config

import uk.gov.hmrc.endtoend.sa.config._

object TestUsers {

  implicit val ggUserWithoutSa = new UserWithGGCredentials {
    val ggCredentials = Credentials("543290000037", "P@ssw0rd")
  }

  implicit val verifyUserWithoutSa = new UserWithVerifyCredentials {
    val verifyCredentials = Credentials("rjeffries", "password")
  }

  implicit val verifyWithOnlyNino = new UserWithVerifyCredentials with UserWithNino {
    val verifyCredentials = Credentials("rjeffries", "password")
    val nino = "ZN522915C"
  }

  implicit val saUserInStub = new UserWithGGCredentials with UserWithUtr {
    val ggCredentials = Credentials("691964262580", "testing123", Some("cred-id-691964262580"))
    val utr = "8040200778"
  }

  implicit def saUserForCurrentEnv: UserWithGGCredentials with UserWithUtr = saUserInStub
}