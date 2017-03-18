package model

import org.joda.time.DateTime

class Suggestion(val name:      String,
                 val id:        String,
                 val version:   String,
                 val content:   String,
                 val upvotes:   Int,
                 val downvotes: Int)

object Suggestion {

  def apply(name:      String,
            id:        String,
            version:   String,
            content:   String,
            upvotes:   Int,
            downvotes: Int) = new Suggestion(name, id, version, content, upvotes, downvotes)

}
