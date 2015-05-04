// Generated rules for Scalataire.
package models.game.rules.generated

import models.game._
import models.game.rules._

/**
 * Original Settings:
 *   Tableau initial cards (T0d): -1 (1 to n cards)
 *   Tableau piles (T0n): 7
 *   Tableau suit match rule for building (T0s): 3 (In same color)
 *   Tableau suit match rule for moving stacks (T0ts): 1 (In same suit)
 */
object Whitehead extends GameRules(
  id = "whitehead",
  title = "Whitehead",
  description = "Like ^klondike^, but with cards face up and you build in matching colors instead of alternating colors. You can move sequences, bu" +
  "t only if the suits match.",
  stock = Some(
    StockRules(
      maximumDeals = Some(1)
    )
  ),
  waste = Some(WasteRules()),
  foundations = Seq(
    FoundationRules(
      numPiles = 4,
      wrapFromKingToAce = true,
      autoMoveCards = true
    )
  ),
  tableaus = Seq(
    TableauRules(
      cardsFaceDown = TableauFaceDownCards.Count(0),
      suitMatchRuleForBuilding = SuitMatchRule.SameColor,
      suitMatchRuleForMovingStacks = SuitMatchRule.SameSuit,
      emptyFilledWith = TableauFillEmptyWith.Aces
    )
  ),
  complete = false
)

