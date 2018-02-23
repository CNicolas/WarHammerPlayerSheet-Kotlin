package warhammer.playersheet.extensions

import warhammer.database.entities.hand.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic

fun Player.createHand(characteristic: Characteristic, name: String = "Hand"): Hand = this[characteristic].getHand(name)

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    availableExperience += experiencePoints
    totalExperience += experiencePoints

    return this
}
