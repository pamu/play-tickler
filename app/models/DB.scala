package models

import java.net.URI

import slick.driver.PostgresDriver.api._
/**
 * Created by pnagarjuna on 26/05/15.
 */
object DB {
  lazy val uri = new URI(s"""postgres://cepjpsajfsbvxz:gc5Htv7jWwb_oPExSd6Tz3ERSz@ec2-54-83-25-238.compute-1.amazonaws.com:5432/daos93l04q2jcl""")

  lazy val username = uri.getUserInfo.split(":")(0)

  lazy val password = uri.getUserInfo.split(":")(1)


  lazy val db = Database.forURL(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
    password = password
  )

  /*
  lazy val db = Database.forURL(
    url = s"jdbc:mysql://localhost/tickler",
    driver = "com.mysql.jdbc.Driver",
    user="root",
    password="root")
    */
}
