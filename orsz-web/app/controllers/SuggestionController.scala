package controllers

import javax.inject._
import play.api._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._
import scala.util._

import model.Suggestion
import persistence.Persistence

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SuggestionController @Inject() extends Controller {

  implicit val suggestionWrite = new Writes[Suggestion] {
    def writes(sugg: Suggestion) = Json.obj(
      "name"      -> sugg.name,
      "id"        -> sugg.id,
      "version"   -> sugg.version,
      "content"   -> sugg.content,
      "upvotes"   -> sugg.upvotes,
      "downvotes" -> sugg.downvotes
    )
  }

  implicit val suggestionRead: Reads[Suggestion] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "id").read[String] and
    (JsPath \ "version").read[String] and
    (JsPath \ "content").read[String] and
    (JsPath \ "upvotes").read[Int] and
    (JsPath \ "downvotes").read[Int]
  )(Suggestion.apply _)

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.main())
  }

  def suggestions = Action {
    Persistence.suggestions match {
      case Success(suggs) => Ok(Json.toJson(suggs))
      case Failure(e)     => InternalServerError
    }
  }

  def suggestion(id: String) = Action {
    Ok(Json.toJson(List(Suggestion(name    = "vini",
                                   id      = id,
                                   version = "1.0",
                                   content = "Standovi si vendono nascere i disordini",
                                   upvotes = 1,
                                   downvotes = 0))))
  }

  def saveSuggestion(id: String) = Action(parse.json[Suggestion]) {
    implicit request =>
      Persistence.persistSuggestion(request.body) match {
        case Success(sugg) => Ok(Json.toJson(sugg))
        case Failure(e)    => InternalServerError
      }
  }

  def removeSuggestion(id: String) = Action {
    Persistence.removeSuggestion(id) match {
      case Success(_) => Ok(id)
      case Failure(e) => InternalServerError
    }
  }

}
