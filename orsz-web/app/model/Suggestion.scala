package model

import org.joda.time.DateTime
import play.api.libs.json._

class Suggestion(val name:      String,
                 val id:        String,
                 val version:   String,
                 val content:   String,
                 val upvotes:   Int,
                 val downvotes: Int)

object Suggestion {

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

  def apply(name:      String,
            id:        String,
            version:   String,
            content:   String,
            upvotes:   Int,
            downvotes: Int) = new Suggestion(name, id, version, content, upvotes, downvotes)

  def apply(suggJson: String) = {
    Json.parse(suggJson).validate[Suggestion] match {
      case s: JsSuccess[Suggestion] => s.get
      case e: JsError => Suggestion(null, null, null, null, null, null)
    }
  }
}
