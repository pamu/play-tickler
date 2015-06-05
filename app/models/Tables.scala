package models

import java.sql.Timestamp

import models.Models.{Tickle, User}
import slick.driver.PostgresDriver.api._
/**
 * Created by pnagarjuna on 27/05/15.
 */
object Tables {
  val userTable = "users"
  class Users(tag: Tag) extends Table[User](tag, userTable) {
    def userId = column[String]("userId")
    def apiKey = column[String]("apiKey")
    def email = column[String]("email")
    def password = column[String]("password")
    def id = column[Long]("id", O.PrimaryKey)
    def * = (userId, apiKey, email, password, id.?) <> (User.tupled, User.unapply)
  }

  val ticklesTable = "tickles"
  class Tickles(tag: Tag) extends Table[Tickle](tag, ticklesTable) {
    def text = column[String]("text")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def * = (text, timestamp, id.?) <> (Tickle.tupled, Tickle.unapply)
  }
  val users = TableQuery[Users]
  val tickles = TableQuery[Tickles]

}
