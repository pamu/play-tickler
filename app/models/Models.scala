package models

import java.sql.Timestamp

/**
 * Created by pnagarjuna on 26/05/15.
 */
object Models {
  case class User(userId: String, apiKey: String, email: String, password: String, id: Option[Long] = None)
  //case class Tickle(userId: String, text: String, timestamp: Timestamp, id: Option[Long] = None)
  case class Tickle(text: String, timestamp: Timestamp, id: Option[Long] = None)
}
