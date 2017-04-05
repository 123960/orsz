package controllers

import scala.util._
import scala.concurrent.ExecutionContext.Implicits.global

import javax.inject._
import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._

import model.Proposition
import persistence.Persistence
import persistence.AsyncPersistence

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PropositionController @Inject() extends Controller {

  lazy val persistence = Persistence.instance

  lazy val asyncPersistence = AsyncPersistence.instance

  implicit val propositionWrite = new Writes[Proposition] {
    def writes(prop: Proposition) = Json.obj(
      "id"         -> prop.id,
      "owner"      -> prop.owner,
      "createDate" -> prop.createDate,
      "name"       -> prop.name,
      "version"    -> prop.version,
      "content"    -> prop.content,
      "upvotes"    -> prop.upvotes,
      "downvotes"  -> prop.downvotes,
      "views"      -> prop.views
    )
  }

  implicit val propositionRead: Reads[Proposition] = (
    (JsPath \ "id").read[String]         and
    (JsPath \ "owner").read[String]      and
    (JsPath \ "createDate").read[String] and
    (JsPath \ "name").read[String]       and
    (JsPath \ "version").read[String]    and
    (JsPath \ "content").read[String]    and
    (JsPath \ "upvotes").read[Int]       and
    (JsPath \ "downvotes").read[Int]     and
    (JsPath \ "views").read[Int]
  )(Proposition.apply _)

  def main = Action {
    Ok(views.html.main())
  }

  def propositions = Action {
    persistence.propositions match {
      case Success(props) => Ok(Json.toJson(props))
      case Failure(e)     => InternalServerError
    }
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
      Ok("ok")
  }

  def removeProposition(id: String) = Action {
    persistence.removeProposition(id) match {
      case Success(_) => Ok(id)
      case Failure(e) => InternalServerError
    }
  }

}
