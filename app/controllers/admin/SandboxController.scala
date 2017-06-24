package controllers.admin

import akka.util.Timeout
import controllers.BaseController
import utils.FutureUtils.defaultContext
import play.twirl.api.Html
import services.sandbox.SandboxTask.{HtmlSandbox, RunScheduledTask}
import services.sandbox._
import services.scheduled.ScheduledTask
import utils.Application

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val app: Application, scheduledTask: ScheduledTask) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def defaultSandbox() = sandbox("list")

  RunScheduledTask.scheduledTask = Some(scheduledTask)

  def sandbox(key: String) = withAdminSession(key) { implicit request =>
    val sandbox = SandboxTask.withValue(key)
    if (sandbox == HtmlSandbox) {
      Future.successful(Ok(views.html.admin.test.sandbox(java.util.UUID.randomUUID())))
    } else {
      sandbox.run(app).map { result =>
        Ok(Html(result))
      }
    }
  }
}
