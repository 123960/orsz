package persistence.impl

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

import scala.util._
import scala.collection.JavaConversions._

import model.Proposition
import persistence.Persistence

object CassandraPersistence extends Persistence {

  lazy val cluster: Cluster = Cluster.builder().addContactPoint("172.22.4.2").build();
  lazy val session: Session = cluster.connect("orsz");

  def persistProposition(prop: Proposition): Try[Proposition] = {
    session.execute(s"INSERT INTO PROPOSITION (name, id, version, content, upvotes, downvotes) VALUES ('${prop.name}', '${prop.id}', '${prop.version}', '${prop.content}', ${prop.upvotes}, ${prop.downvotes})");
    Success(prop)
  }

  def propositions: Try[List[Proposition]] = {
    val props = session.execute(s"SELECT * FROM Proposition")
                       .all
                       .map(row => Proposition(id         = row.getString("id"),
                                               owner      = row.getString("owner"),
                                               createDate = row.getString("create_date"),
                                               name       = row.getString("name"),
                                               version    = row.getString("version"),
                                               content    = row.getString("content"),
                                               upvotes    = row.getInt("upvotes"),
                                               downvotes  = row.getInt("downvotes"),
                                               views      = row.getInt("views")))
                       .toList;
    Success(props)
  }

  def removeProposition(id: String): Try[String] = {
    session.execute(s"DELETE FROM PROPOSITION WHERE id = '${id}'");
    Success(id)
  }

}
