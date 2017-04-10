package model

class Vote(val id:         String,
           val voter:      String,
           val propId:     String,
           val propOwner:  String,
           val voteType:   String,
           val voteDate:   String,
           val version:    String,
           val status:     String) {

  override def toString(): String = s"Vote[ID: ${this.id}, VOTER: ${this.voter}, PROPID: ${this.propId}, VOTETYPE: ${this.voteType}, VERSION: ${this.version}, STATUS: ${this.status}]"

}

object Vote {

  def apply(id:         String,
            voter:      String,
            propId:     String,
            propOwner:  String,
            voteType:   String,
            voteDate:   String,
            version:    String,
            status:     String) = new Vote(id, voter, propId, propOwner,
                                           voteType, voteDate, version, status)

}
