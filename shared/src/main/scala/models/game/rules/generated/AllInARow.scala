// Generated rules for Scalataire.
package models.game.rules.generated

import models.game._
import models.game.rules._

/**
 * Original Settings:
 *   Foundation low rank (F0b): 20 (Any Card)
 *   Foundation initial cards (F0d): 0 (None)
 *   Maximum cards for foundation (F0m): 0
 *   Number of foundation piles (F0n): 1 (1 stack)
 *   Foundation rank match rule (F0r): 0x0020|0x0080
 *   Foundation suit match rule (F0s): 5 (Regardless of suit)
 *   Foundation wraps from king to ace (F0w): true
 *   *S0cardsShown (S0cardsShown): 16
 *   Enable stock (Sn): 0 (No stock)
 *   Tableau initial cards (T0d): 4 (4 cards)
 *   Empty tableau is filled with (T0f): 5 (No card)
 *   Tableau piles (T0n): 13
 *   Tableau rank match rule for building (T0r): 0x0000
 *   Tableau suit match rule for building (T0s): 0 (May not build)
 *   Number of waste piles (W0n): 0
 *   Deal cards from stock (dealto): 6 (To all foundation piles)
 *   Left mouse interface function (leftfunc): 0x2
 *   Similar to (like): golf
 *   Related games (related): blackhole
 *   Touch interface function (touchfunc): 0x2
 *   Victory condition (victory): 5 (All cards on foundation or stock)
 */
object AllInARow extends GameRules(
  id = "allinarow",
  title = "All in a Row",
  like = Some("golf"),
  description = "A variation of ^golf^ without a stock. Most deals are winnable, but require a lot of advance planning to win.",
  victoryCondition = VictoryCondition.AllOnFoundationOrStock,
  foundations = Seq(
    FoundationRules(
      lowRank = FoundationLowRank.AnyCard,
      suitMatchRule = SuitMatchRule.Any,
      rankMatchRule = RankMatchRule.UpOrDown,
      wrapFromKingToAce = true,
      maxCards = 0,
      autoMoveCards = true
    )
  ),
  tableaus = Seq(
    TableauRules(
      numPiles = 13,
      initialCards = InitialCards.Count(4),
      cardsFaceDown = TableauFaceDownCards.Count(0),
      suitMatchRuleForBuilding = SuitMatchRule.None,
      rankMatchRuleForBuilding = RankMatchRule.None,
      suitMatchRuleForMovingStacks = SuitMatchRule.None
    )
  ),
  complete = false
)

