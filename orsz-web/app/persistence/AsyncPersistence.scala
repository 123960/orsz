package persistence

import scala.concurrent.Future

import play.api.libs.ws._

import model.Proposition
import persistence.impl.FirebasePersistence

abstract class AsyncPersistence {

  def persistProposition(prop: Proposition): Future[Proposition]
  def propositionsByOwner(owner: String): Future[List[Proposition]]
  def removeProposition(prop: Proposition): Future[Proposition]

}

object AsyncPersistence {
  def instance(ws: WSClient) = new FirebasePersistence(ws)
}
