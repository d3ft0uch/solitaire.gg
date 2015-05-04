// Generated rules for Scalataire.
package models.game.rules.generated

import models.game._
import models.game.rules._

/**
 * Original Settings:
 *   Deal order (RDd): 0|0|0
 *   Allowed pick ups/redeals (RDn): 2 (2)
 *   Pickup order (RDp): 1|2|0
 *   Enable stock (Sn): 0 (No stock)
 *   Tableau initial cards (T0d): 8 (8 cards)
 *   Tableau piles (T0n): 8
 *   Tableau suit match rule for building (T0s): 4 (In alternating colors)
 *   Number of decks (ndecks): 2 (2 decks)
 *   Ranks in use (ranks): 1|64|128|256|512|1024|2048|4096
 */
object Strata extends GameRules(
  id = "strata",
  title = "Strata",
  description = "An eight-by-eight square tableau, a short deck, and two redeals make this game interesting.",
  deckOptions = DeckOptions(
    numDecks = 2,
    ranks = Seq(Rank.Seven, Rank.Eight, Rank.Nine, Rank.Ten, Rank.Jack, Rank.Queen, Rank.King, Rank.Ace)
  ),
  waste = Some(WasteRules()),
  foundations = Seq(
    FoundationRules(
      numPiles = 8,
      wrapFromKingToAce = true,
      autoMoveCards = true
    )
  ),
  tableaus = Seq(
    TableauRules(
      numPiles = 8,
      initialCards = InitialCards.Count(8),
      cardsFaceDown = TableauFaceDownCards.Count(0),
      suitMatchRuleForMovingStacks = SuitMatchRule.None,
      emptyFilledWith = TableauFillEmptyWith.Aces
    )
  ),
  special = Some(
    SpecialRules(
      redealsAllowed = 2,
      pickupOrder = DealOrder.ColumnsRightToLeftBottomToTop,
      shuffleBeforeRedeal = false,
      dealOrder = DealOrder.RowsLeftToRightTopToBottom
    )
  ),
  complete = false
)

