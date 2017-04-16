package model

class Comment(val id:        String,
              val propId:    String,
              val user:      String,
              val commentType: String,
              val content:   String,
              val commentDate: String) {

  override def toString(): String = s"Comment[ID: ${this.id}, PROPID: ${this.propId}, USER: ${this.user}, TYPE: ${this.commentType}]"

}

object Comment {

  def apply(id:          String,
            propId:      String,
            user:        String,
            commentType: String,
            content:     String,
            commentDate: String) = new Comment(id, propId, user,
                                               commentType, content, commentDate)

}
