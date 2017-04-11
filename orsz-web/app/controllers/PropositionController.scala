package controllers

import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject._

import play.api._
import play.api.mvc._
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
class PropositionController @Inject()(ws: WSClient) extends Controller {

  lazy val persistence = Persistence.instance

  lazy val asyncPersistence = AsyncPersistence.instance(ws)

  def mainPage = Action {
    val props = Await.result(asyncPersistence.propositionsByOwner("vini"), 10 seconds)
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
      val prop = Proposition(id         = if (request.body.id == "<genId>") java.util.UUID.randomUUID().toString() else request.body.id,
                             owner      = request.body.owner,
                             createDate = request.body.createDate,
                             name       = request.body.name,
                             version    = request.body.version,
                             content    = request.body.content,
                             status     = request.body.status,
                             upvotes    = request.body.upvotes,
                             downvotes  = request.body.downvotes,
                             views      = request.body.views)
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
      val f = asyncPersistence.voteProposition(request.body)

      f onSuccess {
        case vote => println(s"[PropositionController.voteProposition] - Vote ${vote} successfully!")
      }
      f onFailure {
        case ex => println(s"[PropositionController.voteProposition] - Failed to vote ${request.body}.")
                   ex.printStackTrace
      }
      Ok("Submited")
  }

}
