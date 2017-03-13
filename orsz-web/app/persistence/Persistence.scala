package persistence

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

import model.Suggestion

object Persistence {

  lazy val cluster: Cluster = Cluster.builder().addContactPoint("172.22.4.2").build();
  lazy val session: Session = cluster.connect("orsz");

  def persistSuggestion(sugg: Suggestion) = {
    session.execute(s"INSERT INTO SUGGESTION (name, id, version, content, upvotes, downvotes) VALUES ('${sugg.name}', '${sugg.id}', '${sugg.version}', '${sugg.content}', ${sugg.upvotes}, ${sugg.downvotes})");
  }

}
