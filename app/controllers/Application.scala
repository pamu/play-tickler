package controllers

import java.sql.Timestamp
import java.util.Date

import models.DAO
import models.Models.Tickle
import play.api.Routes
import play.api.libs.json.{JsValue, Writes, JsError, Json}
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

  implicit val tickleWrites: Writes[(Long, String)] = new Writes[(Long, String)] {
    override def writes(o: (Long, String)): JsValue = {
      Json.obj("id" -> o._1, "text" -> o._2)
    }
  }
  def tickles(page: Int, pageSize: Int) = Action.async { request =>
    DAO.getTickles(page, pageSize)
    .map{ seq =>
      if(!seq.isEmpty) Ok(Json.obj("success" -> Json.obj("tickles" -> Json.toJson(seq))))
      else Ok(Json.obj("failure" -> "empty"))
      }.recover {case th => Ok(Json.obj("failure" -> Json.obj("reason" -> th.getMessage)))}
  }

  def javascriptRoutes() = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(
      controllers.routes.javascript.Application.tickles,
      controllers.routes.javascript.Application.tickle
    )).as(JAVASCRIPT)
  }
}