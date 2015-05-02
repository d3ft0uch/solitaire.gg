package models.game.pile.actions

import models.{ ResponseMessage, CardMoved }
import models.game.Rank._
import models.game.{ Rank, GameState, Card }
import models.game.pile.Pile

case class SelectCardAction(id: String, f: (Pile, Card, GameState) => Seq[ResponseMessage])

object SelectCardActions {
  val none = SelectCardAction("none", (pile, card, gameState) => Nil)

  val klondike = SelectCardAction("klondike", (pile, card, gameState) => {
    if (!card.u) {
      card.u = true
      gameState.revealCardToAll(card)
    } else {
      val foundations = gameState.pileSets.filter(_.behavior == "foundation").flatMap(_.piles)
      val foundation = foundations.flatMap { f =>
        if (f.cards.isEmpty && card.r == Rank.Ace) {
          Some(f)
        } else if (f.cards.lastOption.map(_.s) == Some(card.s) && f.cards.lastOption.map(_.r) == Some(Rank.Ace) && card.r == Rank.Two) {
          Some(f)
        } else if (f.cards.lastOption.map(_.s) == Some(card.s) && f.cards.lastOption.map(_.r.value) == Some(card.r.value - 1)) {
          Some(f)
        } else {
          None
        }
      }.headOption
      foundation match {
        case Some(f) => moveCard(card, pile, f, gameState)
        case None => Nil
      }
    }
  })

  def drawToPile(cardsToDraw: Int, drawTo: String, turn: Option[Boolean] = None) = drawToPiles(cardsToDraw, Seq(drawTo), turn)

  def drawToPiles(cardsToDraw: Int, drawTo: Seq[String], turn: Option[Boolean] = None) = {
    SelectCardAction("draw-to-piles", (pile, card, gameState) => {
      drawTo.flatMap { p =>
        val tgt = gameState.pilesById(p)
        val cards = pile.cards.takeRight(cardsToDraw).reverse
        cards.flatMap { card =>
          moveCard(card, pile, tgt, gameState, turn = turn)
        }
      }
    })
  }

  def drawToNonEmptyPiles(cardsToDraw: Int, drawTo: Seq[String], turn: Option[Boolean] = None) = SelectCardAction("draw-to-piles", (pile, card, gameState) => {
    drawTo.flatMap { p =>
      val tgt = gameState.pilesById(p)
      if(tgt.cards.nonEmpty) {
        val cards = pile.cards.takeRight(cardsToDraw).reverse
        cards.flatMap { card =>
          moveCard(card, pile, tgt, gameState, turn = turn)
        }
      } else {
        Nil
      }
    }
  })

  val alternatingRank = SelectCardAction("alternating-rank", (pile, card, gameState) => {
    val foundation = gameState.pilesById("foundation")
    val topCardRank = foundation.cards.last.r
    val selectedCardRank = card.r
    val shouldMove = topCardRank match {
      case King => selectedCardRank == Queen
      case Ace => selectedCardRank == Two
      case Two => selectedCardRank == Ace || selectedCardRank == Three
      case _ => topCardRank.value == selectedCardRank.value + 1 || topCardRank.value == selectedCardRank.value - 1
    }
    if (shouldMove) {
      moveCard(card, pile, foundation, gameState)
    } else {
      Nil
    }
  })

  def drawToEmptyPiles(behavior: String) = SelectCardAction("draw-to-empty", (pile, card, gameState) => {
    val piles = gameState.pileSets.filter(_.behavior == behavior).flatMap(_.piles)
    piles.flatMap { p =>
      if (p.cards.isEmpty) {
        pile.cards.lastOption match {
          case Some(tc) => moveCard(tc, pile, p, gameState, turn = Some(true))
          case None => Nil
        }
      } else {
        Nil
      }
    }
  })

  private[this] def moveCard(card: Card, src: Pile, tgt: Pile, gameState: GameState, turn: Option[Boolean] = None) = {
    src.removeCard(card)
    tgt.addCard(card)
    val msg = CardMoved(card.id, src.id, tgt.id, turn = turn)
    if (turn.exists(x => x) && !card.u) {
      card.u = true
      val revealed = gameState.revealCardToAll(card)
      revealed ++ Seq(msg)
    } else {
      Seq(msg)
    }
  }
}
