package model

class Proposition(val id:         String,
                  val owner:      String,
                  val createDate: String,
                  val name:       String,
                  val version:    String,
                  val content:    String,
                  val status:     String,
                  val upvotes:    Int,
                  val downvotes:  Int,
                  val views:      Int) {

  override def toString(): String = s"Proposition[ID: ${this.id}, OWNER: ${this.owner}, VERSION: ${this.version}, STATUS: ${this.status}]"

}

object Proposition {

  def apply(id:         String,
            owner:      String,
            createDate: String,
            name:       String,
            version:    String,
            content:    String,
            status:     String,
            upvotes:    Int,
            downvotes:  Int,
            views:      Int) = new Proposition(id, owner, createDate, name, version,
                                               content, status, upvotes, downvotes, views)

}
