package controllers

import akka.actor.{Props, ActorRef}
import models.DAO
import models.Models.{ApiUser, WebUser}
import play.api.mvc._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

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
    DAO.getWebUser(userId).map(webUser => f(webUser)(request)).recoverWith{case throwable => Future(Results.BadRequest("invalid user id"))}
  }}
  def apiCallAccessToken(requestHeader: RequestHeader) = requestHeader.headers.get("accessToken")
  def apiCallUnauthorized(requestHeader: RequestHeader) = Results.Forbidden("Illegal Access")
  def withApiAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(apiCallAccessToken, apiCallUnauthorized) { accessToken => {
      Action.async(parser)(request => f(accessToken)(request))
    }}
  def withApiUser[A](parser: BodyParser[A])(f: ApiUser => Request[A] => Future[Result]) =
    withApiAuth(parser) { accessToken => request => {
      DAO.getApiUser(accessToken).map(apiUser => f(apiUser)(request))
        .recoverWith[Result]{ case throwable => Future(Results.BadRequest("invalid api access token"))}
    }}
  def withWebSocketAuth[A, B](f: => String => RequestHeader => ActorRef => Props) = WebSocket.tryAcceptWithActor[A, B] { request =>
    Future.successful {
      userId(request).map { userId =>
        Right(f(userId)(request)(_))
      }.getOrElse(Left(Results.Forbidden))
    }
  }
  def withWebSocketUser[A, B](f: WebUser => RequestHeader => ActorRef => Props) = WebSocket.tryAcceptWithActor[A, B] { request =>
    userId(request).map { userId =>
      DAO.getWebUser(userId).map(webUser => Right(f(webUser)(request)(_))).recover {case throwable => Left(Results.Forbidden)}
    }.getOrElse(Future(Left(Results.Forbidden)))
  }
}
