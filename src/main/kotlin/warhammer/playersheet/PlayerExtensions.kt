package warhammer.playersheet

import warhammer.database.entities.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.Characteristic

fun Player.createHand(characteristic: Characteristic, name: String = "Hand"): Hand = characteristics[characteristic].getHand(name)

fun Player.earnExperiencePoints(experiencePoints: Int): Player =
        this.copy(
                state = state.copy(
                        career = state.career.copy(
                                totalExperience = state.career.totalExperience!! + experiencePoints,
                                availableExperience = state.career.totalExperience!! + experiencePoints)
                )
        )
