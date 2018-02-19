package warhammer.playersheet.player.extensions

import warhammer.database.entities.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.Characteristic

fun Player.createHand(characteristic: Characteristic, name: String = "Hand"): Hand = characteristics[characteristic].getHand(name)

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    career.availableExperience += experiencePoints
    career.totalExperience += experiencePoints

    return this
}