package warhammer.playersheet.extensions

import warhammer.database.entities.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.Characteristic
import warhammer.database.entities.player.other.Race

fun Player.createHand(characteristic: Characteristic, name: String = "Hand"): Hand = characteristics[characteristic].getHand(name)

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    career.availableExperience += experiencePoints
    career.totalExperience += experiencePoints

    return this
}

fun Player.setAutomaticFields(): Player = calculateMaxFields().calculateEncumbrance()

private fun Player.calculateMaxFields(): Player {
    state.maxStress = willpower.value * 2
    state.maxExhaustion = toughness.value * 2
    inventory.maxEncumbrance = strength.value * 5 + strength.fortuneValue + ENCUMBRANCE_OVERLOAD_TO_MAX + when (race) {
        Race.DWARF -> 5
        else -> 0
    }

    return this
}