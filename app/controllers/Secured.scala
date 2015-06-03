package controllers

import akka.actor.{Props, ActorRef}
import models.DAO
import models.Models.User
import play.api.mvc._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 26/05/15.
 */
trait Secured {
  def userId(requestHeader: RequestHeader) = requestHeader.session.get("userId")
  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login)
  def withAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) = Security.Authenticated(userId, onUnauthorized) { userId => {
    Action.async(parser)(request => f(userId)(request))
  }}
  def withWebUser[A](parser: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(parser) { userId => request => {
    DAO.getUser(userId).map(user => f(user)(request)).recoverWith[Result]{case throwable => Future(Results.BadRequest("invalid user id"))}
  }}
  def apiKey(requestHeader: RequestHeader) = requestHeader.headers.get("apiKey")
  def apiCallUnauthorized(requestHeader: RequestHeader) = Results.Forbidden("Illegal Access")
  def withApiAuth[A](parser: BodyParser[A])(f: => String => Request[A] => Future[Result]) =
    Security.Authenticated(apiKey, apiCallUnauthorized) { apiKey => {
      Action.async(parser)(request => f(apiKey)(request))
    }}
  def withApiUser[A](parser: BodyParser[A])(f: User => Request[A] => Future[Result]) =
    withApiAuth(parser) { apiKey => request => {
      DAO.getUserWithApiKey(apiKey).map(user => f(user)(request))
        .recoverWith[Result]{ case throwable => Future(Results.BadRequest("invalid api access token"))}
    }}
  def withWebSocketAuth[A, B](f: => String => RequestHeader => ActorRef => Props) = WebSocket.tryAcceptWithActor[A, B] { request =>
    Future.successful {
      userId(request).map { userId =>
        Right(f(userId)(request)(_))
      }.getOrElse(Left(Results.Forbidden))
    }
  }
  def withWebSocketUser[A, B](f: User => RequestHeader => ActorRef => Props) = WebSocket.tryAcceptWithActor[A, B] { request =>
    userId(request).map { userId =>
      DAO.getUser(userId).map(user => Right(f(user)(request)(_))).recover {case throwable => Left(Results.Forbidden)}
    }.getOrElse(Future(Left(Results.Forbidden)))
  }
}
