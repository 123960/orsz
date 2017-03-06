package model

import org.joda.time.DateTime

class Suggestion(val name:      String,
                 val id:        String,
                 val version:   (DateTime, String),
                 val content:   String,
                 val upvotes:   (DateTime, String),
                 val downvotes: (DateTime, String))

object Suggestion {
  def apply(name:      String,
            id:        String,
            version:   (DateTime, String),
            content:   String,
            upvotes:   (DateTime, String),
            downvotes: (DateTime, String)) = new Suggestion(name, id, version, content, upvotes, downvotes)
}
