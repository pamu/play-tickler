package models

import models.Models.{ApiUser, WebUser}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
 * Created by pnagarjuna on 26/05/15.
 */
object DAO {
  def getWebUser(userId: String): Future[WebUser] = {
    val q = for(webUser <- Tables.webUsers.filter(_.userId === userId)) yield webUser
    DB.db.run(q.result).map(_ head)
  }
  def getApiUser(accessToken: String): Future[ApiUser] = {
    val q = for(apiUser <- Tables.apiUsers.filter(_.accessToken == accessToken)) yield apiUser
    DB.db.run(q.result).map(_ head)
  }
}
