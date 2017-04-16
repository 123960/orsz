package model

class Vote(val id:         String,
           val voter:      String,
           val propId:     String,
           val propOwner:  String,
           val voteType:   String,
           val voteDate:   String) {

  override def toString(): String = s"Vote[ID: ${this.id}, VOTER: ${this.voter}, PROPID: ${this.propId}, VOTETYPE: ${this.voteType}]"

}

object Vote {

  def apply(id:         String,
            voter:      String,
            propId:     String,
            propOwner:  String,
            voteType:   String,
            voteDate:   String) = new Vote(id, voter, propId, propOwner,
                                           voteType, voteDate)

}
