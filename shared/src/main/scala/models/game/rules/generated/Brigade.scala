// Generated rules for Scalataire.
package models.game.rules.generated

import models.game._
import models.game.rules._

/**
 * Original Settings:
 *   Foundation initial cards (F0d): -1
 *   Enable stock (Sn): 0 (No stock)
 *   Tableau name (T0Nm): Tableau
 *   Tableau initial cards (T0d): 5 (5 cards)
 *   Tableau piles (T0n): 7
 *   Tableau suit match rule for building (T0s): 5 (Regardless of suit)
 *   Waste name (W0Nm): Reserve
 *   Playable waste cards (W0a): true
 *   Number of cards shown (W0cardsShown): 20
 *   *W0s (W0s): true
 *   Similar to (like): flowergarden
 *   Related games (related): wildflower, brigade
 */
object Brigade extends GameRules(
  id = "brigade",
  title = "Brigade",
  like = Some("flowergarden"),
  description = "An easier variation of ^flowergarden^ with more tableau piles of fewer cards, and aces starting on the foundation.",
  waste = Some(
    WasteRules(
      name = "Reserve",
      cardsShown = 20
    )
  ),
  foundations = Seq(
    FoundationRules(
      numPiles = 4,
      initialCards = 4,
      wrapFromKingToAce = true,
      autoMoveCards = true
    )
  ),
  tableaus = Seq(
    TableauRules(
      initialCards = InitialCards.Count(5),
      cardsFaceDown = TableauFaceDownCards.Count(0),
      suitMatchRuleForBuilding = SuitMatchRule.Any,
      suitMatchRuleForMovingStacks = SuitMatchRule.None,
      emptyFilledWith = TableauFillEmptyWith.Aces
    )
  ),
  complete = false
)

