package persistence.impl

import com.typesafe.config.ConfigFactory
import play.api.Logger
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}
import scala.async.Async.{async, await}
import scala.util.{Try, Success, Failure}

import model.Proposition
import model.Vote
import model.implicits.Implicits._
import persistence.AsyncPersistence

class FirebasePersistence(val ws: WSClient) extends AsyncPersistence {

  val conf = ConfigFactory.load()
  val logger: Logger = Logger(classOf[FirebasePersistence])
  val FIREBASE_ORSZ_ENDPOINT = conf.getString("firebase.database")

  /*** READ ***/
  def propositionById(id: String): Future[Proposition] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${id}.json"
    Logger.debug(s"[propositionsById][id: ${id}] - start GET on [${endpoint}]")
    val response = await { ws.url(endpoint).get() }
    Logger.debug(s"[propositionsById][id: ${id}] - end GET on [${endpoint}]. Response was ${response.status}")
    response.status match {
      case 200 => Logger.debug(s"[propositionsById][id: ${id}] - Parsing response body:[${response.body}]")
                  response.json.as[Proposition]
      case _   => throw new Exception(s"[propositionsById][id: ${id}] - Failed to GET on [${endpoint}]. Response was ${response.status}")
    }
  }

  def propositionsByOwner(owner: String): Future[List[Proposition]] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/.json?orderBy=" + "\"owner\"&equalTo=\"" + owner + "\""
    Logger.debug(s"[propositionsByOwner][owner: ${owner}] - start GET on [${endpoint}]")
    val response = await { ws.url(endpoint).get() }
    Logger.debug(s"[propositionsByOwner][owner: ${owner}] - end GET on [${endpoint}]. Response was ${response.status}")
    response.status match {
      case 200 => Logger.debug(s"[propositionsByOwner][owner: ${owner}] - Parsing response body:[${response.body}]")
                  response.json.as[Map[String, JsValue]].values.map(p => p.as[Proposition]).toList
      case _   => throw new Exception(s"[propositionsByOwner][owner: ${owner}] - Failed to GET on [${endpoint}]. Response was ${response.status}")
    }
  }

  def getPropVoteCount(propId: String, voteType: String): Future[Int] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${propId}/${voteType}s.json"
    Logger.debug(s"[syncGetPropVoteCount][propId: ${propId}, voteType: ${voteType}] - start GET on [${endpoint}]")
    val response = await { ws.url(endpoint).get() }
    Logger.debug(s"[syncGetPropVoteCount][propId: ${propId}, voteType: ${voteType}] - end GET on [${endpoint}]. Response was ${response.status}")
    response.status match {
      case 200|204    => response.body.toInt
      case _          => throw new Exception(s"[syncGetPropVoteCount][propId: ${propId}, voteType: ${voteType}] - Failed to GET on [${endpoint}]. Response was ${response.status}")
    }
  }

  /*** UPSERT ***/
  def persistProposition(prop: Proposition): Future[Proposition] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${prop.id}.json?print=silent"
    val content  = Json.toJson(prop)
    Logger.debug(s"[persistProposition][prop: ${prop}] - start PUT on [${endpoint}] with [$content]")
    val response = await { ws.url(endpoint).put(content) }
    Logger.debug(s"[persistProposition][prop: ${prop}] - end PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    response.status match {
      case 200|204 => prop
      case _ => throw new Exception(s"[persistProposition][prop: ${prop}] - Failed to PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    }
  }

  def patchVoteOnProp(vote: Vote, voteCount: Int): Future[Vote] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${vote.propId}.json?print=silent"
    val content  = "{\"" + s"${vote.voteType}s" + "\":" + s"${voteCount + 1}}"
    Logger.debug(s"[syncPatchVoteOnProp][vote: ${vote}] - start PATCH on [${endpoint}] with [$content]")
    val response = await { ws.url(endpoint).patch(content) }
    Logger.debug(s"[syncPatchVoteOnProp][vote: ${vote}] - end PATCH on [${endpoint}] with [$content]. Response was ${response.status}")
    response.status match {
      case 200|204 => vote
      case _          => throw new Exception(s"[syncPatchVoteOnProp][vote: ${vote}] - Failed to PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    }
  }

  private def persistVote(vote: Vote): Future[Vote] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/votes/${vote.id}.json?print=silent"
    val content  = Json.toJson(vote)
    Logger.debug(s"[syncPersistVote][vote: ${vote}] - start PUT on [${endpoint}] with [$content]")
    val response = await { ws.url(endpoint).put(content) }
    Logger.debug(s"[syncPersistVote][vote: ${vote}] - end PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    response.status match {
      case 200|204 => vote
      case _ => throw new Exception(s"[syncPersistVote][vote: ${vote}] - Failed to PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    }
  }

  def voteProposition(vote: Vote): Future[Vote] = async {
    val voteCount = await { getPropVoteCount(vote.propId, vote.voteType) }
    await { patchVoteOnProp(vote, voteCount) }
    await { persistVote(vote) }
  }

  /*** DELETE/FINAL ***/
  def removeProposition(id: String): Future[String] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${id}.json?print=silent"
    Logger.debug(s"[removeProposition][id: ${id}] - start DELETE on [${endpoint}]")
    val response = await { ws.url(endpoint).delete() }
    Logger.debug(s"[removeProposition][id: ${id}] - end DELETE on [${endpoint}]. Response was ${response.status}")
    response.status match {
      case 200|204 => id
      case _          => throw new Exception(s"[removeProposition][id: ${id}] - Failed to DELETE on [${endpoint}]. Response was ${response.status}")
    }
  }

}
