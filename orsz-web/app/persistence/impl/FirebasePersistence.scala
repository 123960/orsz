package persistence.impl

import java.io.FileInputStream
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseCredential
import com.google.firebase.auth.FirebaseCredentials
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.DatabaseReference

import model.Proposition
import persistence.AsyncPersistence

object FirebasePersistence extends AsyncPersistence {

  lazy val conf = ConfigFactory.load()

  lazy val options = new FirebaseOptions.Builder()
                                        .setCredential(FirebaseCredentials.fromCertificate(new FileInputStream(conf.getString("firebase.credentials.path"))))
                                        .setDatabaseUrl("https://orsz-46852-7fe09.firebaseio.com/")
                                        .build()

  lazy val propsDB = FirebaseDatabase.getInstance().getReference("propositions")

  /** Initialize block to instantiates FirebaseApp **/
  {
    println("[FirebasePersistence] - Initialize FirebaseApp")
    FirebaseApp.initializeApp(options)
    FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)
    println("[FirebasePersistence] - Initialize FirebaseApp Done!")
  }

  def persistProposition(prop: Proposition): Future[Proposition] = Future {
    val task = propsDB.setValue(prop.toBean, new DatabaseReference.CompletionListener() {
      override def onComplete(databaseError: DatabaseError, databaseReference: DatabaseReference) {
        if (databaseError != null) {
          println("Error: " + databaseError.getMessage)
        } else {
          println("[FirebasePersistence.persistProposition] - OK")
        }
      }
    })
    prop
  }

  def propositions: Future[List[Proposition]] = ???

  def removeProposition(id: String): Future[String] = ???

}
