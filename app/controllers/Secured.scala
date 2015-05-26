package controllers

import models.DAO
import models.Models.{ApiUser, WebUser}
import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by pnagarjuna on 26/05/15.
 */
trait Secured {
  def userId(requestHeader: RequestHeader) = requestHeader.session.get("userId")
  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login)
  def withWebAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) = Security.Authenticated(userId, onUnauthorized) { userId => {
    Action.async(parser)(request => f(userId)(request))
  }}
  def withWebUser[A](parser: BodyParser[A])(f: WebUser => Request[A] => Future[Result]) = withWebAuth(parser) { userId => request => {
    DAO.getWebUser(userId).map(webUser => f(webUser)(request)).getOrElse(Future(Results.BadRequest("invalid user id")))
  }}
  def apiCallAccessToken(requestHeader: RequestHeader) = requestHeader.headers.get("accessToken")
  def apiCallUnauthorized(requestHeader: RequestHeader) = Results.Forbidden("Illegal Access")
  def withApiAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(apiCallAccessToken, apiCallUnauthorized) { accessToken => {
      Action.async(parser)(request => f(accessToken)(request))
    }}
  def withApiUser[A](parser: BodyParser[A])(f: ApiUser => Request[A] => Future[Result]) =
    withApiAuth(parser) { accessToken => request => {
      DAO.getApiUser(accessToken).map(apiUser => f(apiUser)(request)).getOrElse(Future(Results.BadRequest("invalid api access token")))
    }}
}
