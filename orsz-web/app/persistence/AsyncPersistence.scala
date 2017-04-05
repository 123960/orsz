package persistence

import scala.concurrent.Future

import model.Proposition
import persistence.impl.FirebasePersistence

abstract class AsyncPersistence {

  def persistProposition(prop: Proposition): Future[Proposition]
  def propositions: Future[List[Proposition]]
  def removeProposition(id: String): Future[String]

}

object AsyncPersistence {
  def instance = FirebasePersistence
}
