package ch.chuv.lren.mip.portal

import eu.hbp.mip.model.Variable
import ch.chuv.lren.woken.messages.datasets.DatasetId
import ch.chuv.lren.woken.messages.query.filters.FilterRule
import ch.chuv.lren.woken.messages.query.filters.FilterRule._
import ch.chuv.lren.woken.messages.query.filters.queryFiltersProtocol._
import org.springframework.stereotype.Component
import spray.json._

import scala.collection.JavaConverters._

@Component
class WokenConversions {

  def toFilterRule(json: String): Option[FilterRule] = json match {
    case "" => None
    case _ => Some(json).map(_.parseJson.convertTo[FilterRule])
  }

  def toDatasets(datasets: java.util.List[Variable]): Set[DatasetId] =
    datasets.asScala.map(v => DatasetId(v.getCode)).toSet

  def toSqlWhere(filter: Option[FilterRule]): String = filter.fold("")(_.toSqlWhere)

}
