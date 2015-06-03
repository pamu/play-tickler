package models

import models.Models.{Tickle, User}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 26/05/15.
 */
object DAO {
  def getUser(userId: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.userId === userId)) yield user
    DB.db.run(q.result).map(_ head)
  }
  def getUserWithApiKey(apiKey: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.apiKey === apiKey)) yield user
    DB.db.run(q.result).map(_ head)
  }
  def saveTickle(tickle: Tickle): Future[Int] = {
    DB.db.run(Tables.tickles += tickle)
  }
  def init(): Future[Unit] = {
    DB.db.run(DBIO.seq(Tables.tickles.schema.create))
  }
}
