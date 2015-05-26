package models

import models.Models.{ApiUser, WebUser}

/**
 * Created by pnagarjuna on 26/05/15.
 */
object DAO {
  def getWebUser(userId: String): Option[WebUser] = ???
  def getApiUser(accessToken: String): Option[ApiUser] = ???
}
