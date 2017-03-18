package persistence

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

import scala.util._
import scala.collection.JavaConversions._

import model.Suggestion

object Persistence {

  lazy val cluster: Cluster = Cluster.builder().addContactPoint("172.22.4.2").build();
  lazy val session: Session = cluster.connect("orsz");

  def persistSuggestion(sugg: Suggestion): Try[Suggestion] = {
    session.execute(s"INSERT INTO SUGGESTION (name, id, version, content, upvotes, downvotes) VALUES ('${sugg.name}', '${sugg.id}', '${sugg.version}', '${sugg.content}', ${sugg.upvotes}, ${sugg.downvotes})");
    Success(sugg)
  }

  def suggestions: Try[List[Suggestion]] = {
    val suggs = session.execute(s"SELECT * FROM SUGGESTION")
                       .all
                       .map(row => Suggestion(name      = row.getString("name"),
                                              id        = row.getString("id"),
                                              version   = row.getString("version"),
                                              content   = row.getString("content"),
                                              upvotes   = row.getInt("upvotes"),
                                              downvotes = row.getInt("downvotes")))
                       .toList;
    Success(suggs)
  }

  def removeSuggestion(id: String): Try[String] = {
    session.execute(s"DELETE FROM SUGGESTION WHERE id = '${id}'");
    Success(id)
  }

}
