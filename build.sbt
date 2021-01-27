name := "authorised-economic-operator-tests"

organization := "uk.gov.hmrc"

scalaVersion := "2.11.8"

version := "1.0"

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

val hmrcRepoHost = java.lang.System.getProperty("hmrc.repo.host", "https://nexus-preview.tax.service.gov.uk")

resolvers ++= Seq(
    "hmrc-snapshots"    at hmrcRepoHost + "/content/repositories/hmrc-snapshots",
    "hmrc-releases"     at hmrcRepoHost + "/content/repositories/hmrc-releases",
    "typesafe-releases" at hmrcRepoHost + "/content/repositories/typesafe-releases",
    "HMRC Bintray"      at "https://dl.bintray.com/hmrc/releases",
    "HMRC Bintray RCs"  at "https://dl.bintray.com/hmrc/release-candidates"
)

libraryDependencies += "com.typesafe.play" %% "play-ws" % "2.3.7" exclude("org.apache.httpcomponents", "httpclient") exclude("org.apache.httpcomponents", "httpcore")

libraryDependencies += "uk.gov.hmrc" %% "browser-test" % "2.0.0"

libraryDependencies += "uk.gov.hmrc" %% "domain" % "5.1.0"

libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/html-reports")

testOptions in Test += Tests.Argument("-oD")

parallelExecution in Test := false

scalacOptions ++= Seq("-feature")

val warmUpPlatform = taskKey[Unit]("Warms up the platform so the apps are ready to rumble")

fullRunTask(warmUpPlatform, Test, "uk.gov.hmrc.integration.tests.PlatformWarmer")