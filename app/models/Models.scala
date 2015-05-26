package models

/**
 * Created by pnagarjuna on 26/05/15.
 */
object Models {
  trait User
  case class WebUser() extends User
  case class ApiUser() extends User
}
