package controllers

import java.sql.Timestamp
import java.util.Date

import models.DAO
import models.Models.Tickle
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller {
  def index = Action { request =>
    Ok(views.html.index("Tickler"))
  }
  case class TickleInfo(text: String)
  implicit val tickleInfoFormat = Json.format[TickleInfo]
  def tickle() = Action.async(parse.json) { request =>
    request.body.validate[TickleInfo].fold(
      invalid => Future(BadRequest(JsError.toFlatJson(invalid))),
      valid => {
        DAO.saveTickle(Tickle(valid.text, new Timestamp(new Date().getTime)))
          .map(status => Ok(Json.obj("success" -> Json.obj("status" -> status))))
          .recover {case th => Ok(Json.obj("failure" -> Json.obj("reason" -> th.getMessage)))}
      }
    )
  }
}