package persistence.impl

import java.io.FileInputStream
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

import model.Proposition
import persistence.AsyncPersistence

object FirebasePersistence extends AsyncPersistence {

  lazy val conf = ConfigFactory.load()

  lazy val options = new FirebaseOptions.Builder()
                                        .setServiceAccount(new FileInputStream(new java.io.File(conf.getString("firebase.credentials.path"))))
                                        .setDatabaseUrl("https://orsz-46852-7fe09.firebaseio.com/")
                                        .build()

  lazy val propsDB = FirebaseDatabase.getInstance().getReference()

  /** Initialize block to instantiates FirebaseApp **/
  {
    println("[FirebasePersistence] - Initialize FirebaseApp")
    FirebaseApp.initializeApp(options)
    println("[FirebasePersistence] - Initialize FirebaseApp Done!")
  }

  def persistProposition(prop: Proposition): Future[Proposition] = Future {
    val task = propsDB.setValue(prop.toBean, new DatabaseReference.CompletionListener() {
      override def onComplete(databaseError: DatabaseError, databaseReference: DatabaseReference) {
        println(1)
        if (databaseError != null) {
          println(databaseError.getMessage)
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
