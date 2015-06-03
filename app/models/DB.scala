package models

import java.net.URI

import slick.driver.MySQLDriver.api._
/**
 * Created by pnagarjuna on 26/05/15.
 */
object DB {
  val uri = new URI(s"""postgres://cepjpsajfsbvxz:gc5Htv7jWwb_oPExSd6Tz3ERSz@ec2-54-83-25-238.compute-1.amazonaws.com:5432/daos93l04q2jcl""")

  val username = uri.getUserInfo.split(":")(0)

  val password = uri.getUserInfo.split(":")(1)

  lazy val db = Database.forURL(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
    password = password
  )
}
