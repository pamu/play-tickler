package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller with Secured {
  def index = Action {
    Ok(views.html.index("Tickler"))
  }
}