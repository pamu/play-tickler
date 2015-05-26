package global

import play.api.{Logger, Application, GlobalSettings}

/**
 * Created by pnagarjuna on 26/05/15.
 */
object Global extends GlobalSettings {
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    Logger info("")
  }

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger info("")
  }
}
