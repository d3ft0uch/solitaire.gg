package controllers.admin

import java.util.UUID

import akka.util.Timeout
import models.database.queries.adhoc.AdHocQueries
import play.api.data.Form
import play.api.data.Forms._
import services.database.Database
import utils.DateUtils
import scala.concurrent.Future
import scala.concurrent.duration._
import controllers.BaseController
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object AdHocQueryController extends BaseController {
  case class QueryExecution(action: String, id: Option[String], title: String, sql: String)

  val executionForm = Form(
    mapping(
      "action" -> nonEmptyText,
      "id" -> optional(text),
      "title" -> text,
      "sql" -> nonEmptyText
    )(QueryExecution.apply)(QueryExecution.unapply)
  )

  implicit val timeout = Timeout(10.seconds)

  def queryList(query: Option[UUID]) = withAdminSession { implicit request =>
    Database.query(AdHocQueries.SearchQuery("", "title", None)).map { queries =>
      val q = query.flatMap(x => queries.find(_.id == x))
      Ok(views.html.admin.report.adhoc(query, q.map(_.sql).getOrElse(""), Seq.empty -> Seq.empty, 0, queries))
    }
  }

  def run() = withAdminSession { implicit request =>
    import DateUtils._

    executionForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest("Couldn't process form:\n  " + formWithErrors.errors.mkString("  \n"))),
      form => form.action match {
        case "save" => if(form.id.isEmpty || form.id.get.isEmpty) {
          Database.execute(AdHocQueries.CreateAdHocQuery(UUID.randomUUID, form.title, request.identity.id, form.sql)).flatMap { ok =>
            Database.query(AdHocQueries.SearchQuery("", "title", None)).map { queries =>
              val newId = queries.sortBy(_.created).headOption.map(_.id)
              Ok(views.html.admin.report.adhoc(newId, form.sql, Seq.empty -> Seq.empty, 0, queries))
            }
          }
        } else {
          val queryId = form.id.map(UUID.fromString)
          Database.execute(AdHocQueries.UpdateAdHocQuery(queryId.get, form.title, request.identity.id, form.sql)).flatMap { ok =>
            Database.query(AdHocQueries.SearchQuery("", "title", None)).map { queries =>
              Ok(views.html.admin.report.adhoc(queryId, form.sql, Seq.empty -> Seq.empty, 0, queries))
            }
          }
        }
        case "run" =>
          Database.query(AdHocQueries.SearchQuery("", "title", None)).flatMap { queries =>
            val startTime = System.currentTimeMillis
            Database.query(AdHocQueries.AdHocQueryExecute(form.sql, Seq.empty)).map { result =>
              val executionTime = (System.currentTimeMillis - startTime).toInt
              val queryId = form.id.map(UUID.fromString)
              Ok(views.html.admin.report.adhoc(queryId, form.sql, result, executionTime, queries))
            }
          }
        case x => throw new IllegalStateException(x.toString)
      }
    )
  }
}
