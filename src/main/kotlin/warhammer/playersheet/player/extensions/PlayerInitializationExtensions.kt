package warhammer.playersheet.player.extensions

import warhammer.database.entities.player.Player
import warhammer.database.entities.player.other.Race

fun Player.setAutomaticFields(): Player =
        calculateMaxFields()
                .calculateEncumbrance()

private fun Player.calculateMaxFields(): Player = copy(
        state = state.copy(
                maxStress = willpower.value * 2,
                maxExhaustion = toughness.value * 2
        ),
        inventory = inventory.copy(
                maxEncumbrance = strength.value * 5
                        + strength.fortuneValue
                        + ENCUMBRANCE_OVERLOAD_TO_MAX
                        + when (race) {
                    Race.DWARF -> 5
                    else -> 0
                }
        )
)

private const val ENCUMBRANCE_OVERLOAD_TO_MAX = 5