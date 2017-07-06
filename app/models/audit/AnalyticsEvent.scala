package models.audit

import enumeratum.values._

object AnalyticsEvent {
  sealed abstract class EventType(val value: String) extends StringEnumEntry

  object EventType extends StringEnum[EventType] with StringCirceEnum[EventType] {
    case object Error extends EventType("error")
    case object Install extends EventType("install")
    case object Open extends EventType("open")
    case object GameStart extends EventType("game-start")
    case object GameWon extends EventType("game-won")
    case object GameResigned extends EventType("game-resigned")
    case class Unknown(id: String) extends EventType(id)

    val all = Seq(Error, Install, Open, GameStart, GameWon, GameResigned)
    def keyMap = (all.map(x => x.value -> x) ++ all.map(x => x.toString -> x)).toMap
    def fromString(s: String) = keyMap.getOrElse(s, Unknown(s))

    override def values = findValues
  }
}
