package model.implicits

import play.api.libs.json._
import play.api.libs.functional.syntax._
import model.Vote
import model.Comment
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
      "status"     -> prop.status,
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
    (JsPath \ "status").read[String]     and
    (JsPath \ "upvotes").read[Int]       and
    (JsPath \ "downvotes").read[Int]     and
    (JsPath \ "views").read[Int]
  )(Proposition.apply _)

  implicit val voteWrite = new Writes[Vote] {
    def writes(vote: Vote) = Json.obj(
      "id"          -> vote.id,
      "voter"       -> vote.voter,
      "propId"      -> vote.propId,
      "propOwner"   -> vote.propOwner,
      "voteType"    -> vote.voteType,
      "voteDate"    -> vote.voteDate
    )
  }

  implicit val voteRead: Reads[Vote] = (
    (JsPath \ "id").read[String]        and
    (JsPath \ "voter").read[String]     and
    (JsPath \ "propId").read[String]    and
    (JsPath \ "propOwner").read[String] and
    (JsPath \ "voteType").read[String]  and
    (JsPath \ "voteDate").read[String]
  )(Vote.apply _)

  implicit val commentWrite = new Writes[Comment] {
    def writes(comment: Comment) = Json.obj(
      "id"           -> comment.id,
      "propId"       -> comment.propId,
      "user"         -> comment.user,
      "commentType"  -> comment.commentType,
      "content"      -> comment.content,
      "commentDate"  -> comment.commentDate
    )
  }

  implicit val commentRead: Reads[Comment] = (
    (JsPath \ "id").read[String]          and
    (JsPath \ "propId").read[String]      and
    (JsPath \ "user").read[String]        and
    (JsPath \ "commentType").read[String] and
    (JsPath \ "content").read[String]     and
    (JsPath \ "commentDate").read[String]
  )(Comment.apply _)

}
