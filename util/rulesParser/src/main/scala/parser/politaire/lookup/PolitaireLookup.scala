package parser.politaire.lookup

object PolitaireLookup {
  val titleTable =
    GameLookup.titles ++
      DeckLookup.titles ++
      FoundationLookup.titles ++
      TableauLookup.titles ++
      StockLookup.titles ++
      WasteLookup.titles ++
      ReserveLookup.titles ++
      CellLookup.titles ++
      PyramidLookup.titles ++
      Seq(
        // Special
        "RDn" -> "Allowed pick ups/redeals",
        "nrot" -> "Allowed tableau rotations",
        "ndraw" -> "Allowed draws"
      )

  private[this] val titleMap = titleTable.toMap

  def getTitle(key: String) = {
    val c = key(1)
    if (c == '1' || c == '2' || c == '3' || c == '4') {
      titleMap.get(key.head + "0" + key.tail.tail)
    } else {
      titleMap.get(key)
    }
  }

  private[this] val translationTable = {
    GameLookup.translations ++
      DeckLookup.translations ++
      FoundationLookup.translations ++
      TableauLookup.translations ++
      StockLookup.translations ++
      WasteLookup.translations ++
      ReserveLookup.translations ++
      CellLookup.translations ++
      PyramidLookup.translations ++
      Map(
        // Special
        "RDn" -> Map(1 -> "1", 2 -> "2", 3 -> "3", 4 -> "4", 5 -> "5", 6 -> "6", 7 -> "7", 8 -> "8", 9 -> "9", 10 -> "10", -1 -> "Unlimited"),
        "nrot" -> Map(1 -> "1", 2 -> "2", 3 -> "3", 4 -> "4", 5 -> "5", 6 -> "6", 7 -> "7", 8 -> "8", 9 -> "9", 10 -> "10", -1 -> "Unlimited"),
        "ndraw" -> Map(1 -> "1", 2 -> "2", 3 -> "3", 4 -> "4", 5 -> "5", 6 -> "6", 7 -> "7", 8 -> "8", 9 -> "9", 10 -> "10", -1 -> "Unlimited")
      )
  }

  def getTranslation(key: String) = {
    val c = key(1)
    if (c == '1' || c == '2' || c == '3' || c == '4') {
      translationTable.get(key.head + "0" + key.tail.tail)
    } else {
      translationTable.get(key)
    }
  }

  def parseBitmask(key: String, i: Int) = {
    val translations = translationTable(key)
    translations.flatMap { t =>
      if ((t._1 & i) != 0) {
        Some(t._2)
      } else {
        None
      }
    }
  }.toSeq
}