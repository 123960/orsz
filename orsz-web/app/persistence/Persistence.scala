package persistence

import scala.util._

import model.Proposition
import persistence.impl.CassandraPersistence

abstract class Persistence {

  def persistProposition(prop: Proposition): Try[Proposition]
  def propositions: Try[List[Proposition]]
  def removeProposition(id: String): Try[String]

}

object Persistence {
  def instance = CassandraPersistence
}
