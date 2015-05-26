package models

/**
 * Created by pnagarjuna on 26/05/15.
 */
object Models {
  trait User
  case class WebUser(userId: String, email: String, password: String, id: Option[Long] = None) extends User
  case class ApiUser(accessToken: Long, email: String, password: String, id: Option[Long] = None) extends User
}
