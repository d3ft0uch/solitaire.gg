// Generated rules for Scalataire.
package models.game.rules.generated

import models.game._
import models.game.rules._

/**
 * Original Settings:
 *   Foundation name (F0Nm): Left Foundation
 *   Auto-move cards to foundation (F0a): 0 (Never)
 *   Can move cards from foundation (F0mb): 1 (Always)
 *   Number of foundation piles (F0n): 4 (4 stacks)
 *   TODO (F0u): 2
 *   Foundation name (F1Nm): Right Foundation
 *   Auto-move cards to foundation (F1a): 0 (Never)
 *   Foundation add complete sequences only (F1cs): true
 *   Can move cards from foundation (F1mb): 1 (Always)
 *   Number of foundation piles (F1n): 4 (4 stacks)
 *   TODO (F1u): 2
 *   Foundation Sets (Fn): 2
 *   Tableau initial cards (T0d): -1 (1 to n cards)
 *   Tableau cards face down (T0df): 100
 *   Empty tableau is filled with (T0f): 1 (Kings only)
 *   Tableau piles (T0n): 10
 *   Tableau suit match rule for building (T0s): 5 (Regardless of suit)
 *   Tableau suit match rule for moving stacks (T0ts): 1 (In same suit)
 *   Maximum deals from stock (maxdeals): 0
 *   Number of decks (ndecks): 2 (2 decks)
 *   Related games (related): fortydevils
 */
object LadyCadogan extends GameRules(
  id = "ladycadogan",
  title = "Lady Cadogan",
  description = "Thomas Warfield's ^rougeetnoir^ variant where we build regardless of suit instead of in alternating colors.",
  deckOptions = DeckOptions(
    numDecks = 2
  ),
  stock = Some(StockRules()),
  waste = Some(WasteRules()),
  foundations = Seq(
    FoundationRules(
      name = "Left Foundation",
      numPiles = 4,
      wrapFromKingToAce = true
    ),
    FoundationRules(
      name = "Right Foundation",
      setNumber = 1,
      numPiles = 4,
      wrapFromKingToAce = true,
      moveCompleteSequencesOnly = true
    )
  ),
  tableaus = Seq(
    TableauRules(
      numPiles = 10,
      suitMatchRuleForBuilding = SuitMatchRule.Any,
      suitMatchRuleForMovingStacks = SuitMatchRule.SameSuit,
      emptyFilledWith = TableauFillEmptyWith.Kings
    )
  ),
  complete = false
)

