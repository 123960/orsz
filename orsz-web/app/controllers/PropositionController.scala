package controllers

import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.cache._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.Await
import scala.concurrent.duration._

import model.Vote
import model.Proposition
import model.implicits.Implicits._
import persistence.Persistence
import persistence.AsyncPersistence

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PropositionController @Inject()(ws: WSClient)(cache: CacheApi) extends Controller {

  lazy val persistence = Persistence.instance

  lazy val asyncPersistence = AsyncPersistence.instance(ws)

  def mainPage = Action {
    val props = cache.getOrElse[List[Proposition]]("propsByOwner.vini") {
      Await.result(asyncPersistence.propositionsByOwner("vini"), 10 seconds)
    }
    cache.set("propsByOwner.vini", props, 5 minutes)
    Ok(views.html.main("Discussion Platform", props))
  }

  def newPropositionPage = Action {
    Ok(views.html.newprop("New Proposition"))
  }

  def propositionById(id: String) = Action {
    val f = asyncPersistence.propositionById(id)
    f onSuccess {
      case prop => println(s"[PropositionController.propositionById] - Propositions: ${prop}.")
    }
    f onFailure {
      case ex => println(s"[PropositionController.propositionById] - Failed to get proposition to  ${id}.")
                 ex.printStackTrace
    }
    Ok("Submited")
  }

  def propositionsByOwner(owner: String) = Action {
    val f = asyncPersistence.propositionsByOwner(owner)
    f onSuccess {
      case props => println(s"[PropositionController.propositionsByOwner] - Propositions: ${props}.")
    }
    f onFailure {
      case ex => println(s"[PropositionController.propositionsByOwner] - Failed to get propositions to  ${owner}.")
                 ex.printStackTrace
    }
    Ok("Submited")
  }

  def saveProposition(id: String) = Action(parse.json[Proposition]) {
    implicit request =>
      val prop = this.genIdFor(request.body)
      val f = asyncPersistence.persistProposition(prop)

      f onSuccess {
        case _ => println(s"[PropositionController.saveProposition] - Proposition: ${prop} saved successfully!")
      }
      f onFailure {
        case ex => println(s"[PropositionController.saveProposition] - Failed to save proposition: ${request.body}.")
                   ex.printStackTrace
      }
      Ok("Submited")
  }

  def removeProposition(id: String) = Action {
      val f = asyncPersistence.removeProposition(id)
      f onSuccess {
        case _ => println(s"[PropositionController.removeProposition] - Proposition ${id} removed successfully!")
      }
      f onFailure {
        case ex => println(s"[PropositionController.removeProposition] - Failed to remove proposition ${id}.")
                   ex.printStackTrace
      }
      Ok("Submited")
  }

  def voteProposition(id: String) = Action(parse.json[Vote]) {
    implicit request =>
      val vote =  this.genIdFor(request.body)
      val f = asyncPersistence.voteProposition(vote)

      f onSuccess {
        case _ => println(s"[PropositionController.voteProposition] - Vote ${vote} successfully!")
      }
      f onFailure {
        case ex => println(s"[PropositionController.voteProposition] - Failed to vote ${request.body}.")
                   ex.printStackTrace
      }
      Ok("Submited")
  }

  def genIdFor(vote: Vote): Vote =
    if (vote.id == "<genId>") Vote(id         = java.util.UUID.randomUUID().toString(),
                                   voter      = vote.voter,
                                   propId     = vote.propId,
                                   propOwner  = vote.propOwner,
                                   voteType   = vote.voteType,
                                   voteDate   = vote.voteDate)
    else vote

  def genIdFor(prop: Proposition): Proposition =
    if (prop.id == "<genId>") Proposition(id         = java.util.UUID.randomUUID().toString(),
                                          owner      = prop.owner,
                                          createDate = prop.createDate,
                                          name       = prop.name,
                                          version    = prop.version,
                                          content    = prop.content,
                                          status     = prop.status,
                                          upvotes    = prop.upvotes,
                                          downvotes  = prop.downvotes,
                                          views      = prop.views)
    else prop

  def sumVote(prop: Proposition, voteType: String): Proposition =
    voteType  match {
      case "upvote"   => Proposition(id         = prop.id,
                                     owner      = prop.owner,
                                     createDate = prop.createDate,
                                     name       = prop.name,
                                     version    = prop.version,
                                     content    = prop.content,
                                     status     = prop.status,
                                     upvotes    = prop.upvotes + 1,
                                     downvotes  = prop.downvotes,
                                     views      = prop.views)
      case "downvote" => Proposition(id         = prop.id,
                                     owner      = prop.owner,
                                     createDate = prop.createDate,
                                     name       = prop.name,
                                     version    = prop.version,
                                     content    = prop.content,
                                     status     = prop.status,
                                     upvotes    = prop.upvotes,
                                     downvotes  = prop.downvotes + 1,
                                     views      = prop.views)
    }
}
