package uk.aeo.mra.data.page

import uk.aeo.mra.data.config.EnvIntegrationPatience
import uk.gov.hmrc.endtoend.sa.{Page, RelativeUrl}

trait EnvPage extends Page with EnvIntegrationPatience

trait Port {
  def port: Int
}

trait AuthStubPage                 extends EnvPage with Port with RelativeUrl                    { val port = 9949 }
trait StrideAuthStubPage           extends EnvPage with Port with RelativeUrl                    { val port = 9041 }