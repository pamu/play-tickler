package actors

import java.sql.Timestamp
import java.util.Date

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import models.DAO
import models.Models.Tickle

import akka.pattern.pipe
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 07/06/15.
 */
object User {
  def props(out: ActorRef) = Props(new User(out))
}

object UserInternalMessages {
  case class Saved(actorRef: ActorRef)
  case class Failed(msg: String, actorRef: ActorRef)
}

class User(out: ActorRef) extends Actor with ActorLogging {
  import UserInternalMessages._
  override def receive = {
    case msg: String =>
      val future = DAO.saveTickle(Tickle(msg, new Timestamp(new Date().getTime)))
      val f = future.map{ x => Saved(out)}.recover{case th => Failed(s"failed due to ${th.getMessage}", out)}
      f pipeTo self
    case Saved(senderMan) => senderMan ! "Saved"
    case Failed(msg, senderMan) => senderMan ! msg
    case ex => log.info("unknown message {} of type {}", ex, ex getClass)
  }
}
