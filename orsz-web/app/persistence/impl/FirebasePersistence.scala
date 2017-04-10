package persistence.impl

import play.api.Logger
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}
import scala.async.Async.{async, await}

import model.Proposition
import model.implicits.Implicits._
import persistence.AsyncPersistence

class FirebasePersistence(val ws: WSClient) extends AsyncPersistence {

  val logger: Logger = Logger(classOf[FirebasePersistence])

  private val FIREBASE_ORSZ_ENDPOINT = "https://orsz-46852-7fe09.firebaseio.com"

  def persistProposition(prop: Proposition): Future[Proposition] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${prop.owner}/${prop.id}.json"
    val content  = Json.toJson(prop)
    Logger.debug(s"[persistProposition][prop: ${prop}] - Starting PUT on [${endpoint}] with [$content]")
    val response = await { ws.url(endpoint).put(content) }
    Logger.debug(s"[persistProposition][prop: ${prop}] - END PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    if (response.status == 200) {
      prop
    } else {
      throw new Exception(s"[persistProposition][prop: ${prop}] - Failed to PUT on [${endpoint}] with [$content]. Response was ${response.status}")
    }
  }

  def propositionsByOwner(owner: String): Future[List[Proposition]] = async {
    val endpoint = s"${FIREBASE_ORSZ_ENDPOINT}/propositions/${owner}.json"
    Logger.debug(s"[propositionsByOwner][owner: ${owner}] - Starting GET on [${endpoint}]")
    val response = await { ws.url(endpoint).get() }
    Logger.debug(s"[propositionsByOwner][owner: ${owner}] - END GET on [${endpoint}]. Response was ${response.status}")
    if (response.status == 200) {
      Logger.debug(s"[propositionsByOwner][owner: ${owner}] - Parsing response body:[${response.body}]")
      response.json.as[JsArray].value.tail.map(p => p.as[Proposition]).toList
    } else {
      throw new Exception(s"[propositionsByOwner][owner: ${owner}] - Failed to GET on [${endpoint}]. Response was ${response.status}")
    }
  }

  def removeProposition(prop: Proposition): Future[Proposition] = ???

}
