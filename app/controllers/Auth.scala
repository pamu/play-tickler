package controllers

import play.api.mvc.{Action, Controller}

/**
 * Created by pnagarjuna on 26/05/15.
 */
object Auth extends Controller {
  def login() = Action { implicit request =>
    Ok("")
  }
}
