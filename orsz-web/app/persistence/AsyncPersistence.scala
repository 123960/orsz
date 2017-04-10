package persistence

import scala.concurrent.Future

import play.api.libs.ws._

import model.Vote
import model.Proposition
import persistence.impl.FirebasePersistence

abstract class AsyncPersistence {

  def persistProposition(prop: Proposition): Future[Proposition]
  def propositionsByOwner(owner: String): Future[List[Proposition]]
  def propositionById(id: String): Future[Proposition]
  def removeProposition(id: String): Future[String]
  def voteProposition(vote: Vote): Future[Vote]

}

object AsyncPersistence {
  def instance(ws: WSClient) = new FirebasePersistence(ws)
}
