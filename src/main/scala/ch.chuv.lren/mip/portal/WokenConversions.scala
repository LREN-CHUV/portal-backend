package ch.chuv.lren.mip.portal

import eu.hbp.mip.woken.messages.datasets.DatasetId
import eu.hbp.mip.woken.messages.query.filters.FilterRule
import eu.hbp.mip.woken.messages.query.filters.queryFiltersProtocol._
import org.springframework.stereotype.Component
import spray.json._

@Component
class WokenConversions {

  def toFilterRule(json: String): Option[FilterRule] = Some(json).map(_.parseJson.convertTo[FilterRule])

  def toDatasets(commaSeparatedSets: String): Set[DatasetId] = commaSeparatedSets match {
    case "" => Set()
    case _  => commaSeparatedSets.split(",").map(DatasetId).toSet
  }

}
