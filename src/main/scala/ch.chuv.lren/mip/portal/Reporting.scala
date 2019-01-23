package ch.chuv.lren.mip.portal

import ch.chuv.lren.woken.errors._

class Reporting {

  def init(): Unit = {
    reportErrorsToBugsnag()
  }

}
