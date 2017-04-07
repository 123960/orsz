package controllers

import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._

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

  def main = Action {
    Ok(views.html.main())
  }

  def propositionsByOwner(owner: String) = Action {
    val f = asyncPersistence.propositionsByOwner(owner)
    f onSuccess {
      case props => println(s"[PropositionController.propositionsByOwner] - Propositions: ${props}")
    }
    f onFailure {
      case ex => println(s"[PropositionController.propositionsByOwner] - Failed to get propositions to  ${owner}: ")
                 ex.printStackTrace
    }
    Ok("Submited")
  }

  def proposition(id: String) = Action {
    Ok(Json.toJson(List(Proposition(id         = id,
                                    owner      = "vini",
                                    createDate = "today",
                                    name       = "vini",
                                    version    = "1.0",
                                    content    = "Standovi si vendono nascere i disordini",
                                    upvotes    = 1,
                                    downvotes  = 0,
                                    views      = 1))))
  }

  def saveProposition(id: String) = Action(parse.json[Proposition]) {
    implicit request =>
      val f = asyncPersistence.persistProposition(request.body)

      f onSuccess {
        case prop => println("[PropositionController.saveProposition] - Proposition: " + prop)
      }
      f onFailure {
        case ex => println("[PropositionController.saveProposition] - Failed to save proposition: " + request.body)
                   ex.printStackTrace
      }
      Ok("Submited")
  }

  def removeProposition(id: String) = Action {
    persistence.removeProposition(id) match {
      case Success(_) => Ok(id)
      case Failure(e) => InternalServerError
    }
  }

}
