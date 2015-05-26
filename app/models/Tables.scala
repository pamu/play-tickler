package models

import models.Models.{ApiUser, WebUser}
import slick.driver.MySQLDriver.api._
/**
 * Created by pnagarjuna on 27/05/15.
 */
object Tables {
  val webUserTable = "web_users"
  class WebUsers(tag: Tag) extends Table[WebUser](tag, webUserTable) {
    def userId = column[String]("userId")
    def email = column[String]("email")
    def password = column[String]("password")
    def id = column[Long]("id", O.PrimaryKey)
    def * = (userId, email, password, id.?) <> (WebUser.tupled, WebUser.unapply)
  }

  val apiUserTable = "api_users"
  class ApiUsers(tag: Tag) extends Table[ApiUser](tag, apiUserTable) {
    def accessToken = column[String]("accessToken")
    def email = column[String]("email")
    def password = column[String]("password")
    def id = column[Long]("id", O.PrimaryKey)
    def * = (accessToken, email, password, id.?) <> (ApiUser.tupled, ApiUser.unapply)
  }

  val webUsers = TableQuery[WebUsers]
  val apiUsers = TableQuery[ApiUsers]
}
