package warhammer.playersheet.extensions

import warhammer.database.entities.hand.DifficultyLevel
import warhammer.database.entities.hand.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic
import warhammer.database.entities.player.playerLinked.skill.Skill

fun Player.createHand(characteristic: Characteristic,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand =
        this[characteristic].getHand(name, difficultyLevel)

fun Player.createHand(skill: Skill,
                      name: String = "Hand",
                      difficultyLevel: DifficultyLevel = DifficultyLevel.NONE): Hand {
    val hand = this.createHand(skill.characteristic, name, difficultyLevel)
    hand.expertiseDicesCount = skill.level

    return hand
}

fun Player.earnExperiencePoints(experiencePoints: Int): Player {
    availableExperience += experiencePoints
    totalExperience += experiencePoints

    return this
}
