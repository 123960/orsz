package persistence.impl

import java.io.FileInputStream
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

import model.Proposition
import persistence.AsyncPersistence

object FirebasePersistence extends AsyncPersistence {

  lazy val conf = ConfigFactory.load()

  lazy val options = new FirebaseOptions.Builder()
                                        .setServiceAccount(new FileInputStream(new java.io.File(conf.getString("firebase.credentials.path"))))
                                        .setDatabaseUrl("https://orsz-46852-7fe09.firebaseio.com")
                                        .build()

  lazy val propsDB = FirebaseDatabase.getInstance()
                                     .getReference()

  /** Initialize block to instantiates FirebaseApp **/
  {
    FirebaseApp.initializeApp(options)
  }

  def persistProposition(prop: Proposition): Future[Proposition] = Future {
    propsDB.child("propositions").child(prop.owner).child(prop.id).setValue(prop.toBean)
    prop
  }

  def propositions: Future[List[Proposition]] = ???

  def removeProposition(id: String): Future[String] = ???

}
