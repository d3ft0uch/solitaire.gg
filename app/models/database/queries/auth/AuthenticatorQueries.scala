package models.database.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.database.queries.BaseQueries
import models.database.{ Row, Statement, FlatSingleRowQuery }
import org.joda.time.LocalDateTime

object AuthenticatorQueries extends BaseQueries[CookieAuthenticator] {
  override protected val tableName = "session_info"
  override protected val columns = Seq("id", "provider", "key", "last_used", "expiration", "fingerprint", "created")
  override protected val searchColumns = Seq("id::text", "key")

  val insert = Insert
  val getById = GetById
  val removeById = RemoveById

  case class FindSessionInfoByLoginInfo(l: LoginInfo) extends FlatSingleRowQuery[CookieAuthenticator] {
    override val sql = getSql(Some("provider = ? and key = ?"))
    override val values = Seq(l.providerID, l.providerKey)
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  case class UpdateAuthenticator(ca: CookieAuthenticator) extends Statement {
    override val sql = updateSql(Seq("provider", "key", "last_used", "expiration", "fingerprint"))
    override val values = Seq(
      ca.loginInfo.providerID,
      ca.loginInfo.providerKey,
      ca.lastUsedDate.toLocalDateTime,
      ca.expirationDate.toLocalDateTime,
      ca.fingerprint,
      ca.id
    )
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[String]("id")
    val provider = row.as[String]("provider")
    val key = row.as[String]("key")
    val lastUsed = row.as[LocalDateTime]("last_used").toDateTime
    val expiration = row.as[LocalDateTime]("expiration").toDateTime
    val fingerprint = row.asOpt[String]("fingerprint")
    val created = row.as[LocalDateTime]("created")
    CookieAuthenticator(id, LoginInfo(provider, key), lastUsed, expiration, None, fingerprint)
  }

  override protected def toDataSeq(ca: CookieAuthenticator) = Seq(
    ca.id,
    ca.loginInfo.providerID,
    ca.loginInfo.providerKey,
    ca.lastUsedDate.toLocalDateTime,
    ca.expirationDate.toLocalDateTime,
    ca.fingerprint,
    new LocalDateTime()
  )
}
