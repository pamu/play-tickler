package actors

import akka.actor.{ActorLogging, Actor}

/**
 * Created by pnagarjuna on 07/06/15.
 */
object User {

}

class User extends Actor with ActorLogging {
  override def receive = {
    case ex =>
  }
}
