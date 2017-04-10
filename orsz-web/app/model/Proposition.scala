package model

class Proposition(val id:         String,
                  val owner:      String,
                  val createDate: String,
                  val name:       String,
                  val version:    String,
                  val content:    String,
                  val upvotes:    Int,
                  val downvotes:  Int,
                  val views:      Int) {

  def toBean = {
    val b = new PropositionBean()
    b.id         = this.id
    b.owner      = this.owner
    b.createDate = this.createDate
    b.name       = this.name
    b.version    = this.version
    b.content    = this.content
    b.upvotes    = this.upvotes
    b.downvotes  = this.downvotes
    b.views      = this.views
    b
  }

  override def toString(): String = s"Proposition[ID: ${this.id}, OWNER: ${this.owner}, VERSION: ${this.version}]"

}

object Proposition {

  def apply(id:         String,
            owner:      String,
            createDate: String,
            name:       String,
            version:    String,
            content:    String,
            upvotes:    Int,
            downvotes:  Int,
            views:      Int) = new Proposition(id, owner, createDate, name, version,
                                               content, upvotes, downvotes, views)

}

class PropositionBean() {
  import scala.beans.BeanProperty
  @BeanProperty var id: String         = null
  @BeanProperty var owner: String      = null
  @BeanProperty var createDate: String = null
  @BeanProperty var name: String       = null
  @BeanProperty var version: String    = null
  @BeanProperty var content: String    = null
  @BeanProperty var upvotes: Int       = 0
  @BeanProperty var downvotes: Int     = 0
  @BeanProperty var views: Int         = 0

  def toProp = Proposition(id, owner, createDate, name, version,
                           content, upvotes, downvotes, views)
}
