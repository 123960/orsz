package model.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import model.Proposition

object Implicits {
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
}
