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

//fun Player.merge(player: Player): Player {
//    return this
//}
//
//fun PlayerState.merge(other: PlayerState): PlayerState {
//    var state = this
//
//    if (playerId != other.playerId) other.playerId
//    if (availableExperience != other.availableExperience)
//}
//
//fun PlayerCharacteristics.merge(other: PlayerCharacteristics): PlayerCharacteristics {
//    var characteristics = this
//
//    if (strength.compareTo(other.strength) != 0) characteristics = characteristics.copy(strength = other.strength)
//    if (toughness.compareTo(other.toughness) != 0) characteristics = characteristics.copy(toughness = other.toughness)
//    if (agility.compareTo(other.agility) != 0) characteristics = characteristics.copy(agility = other.agility)
//    if (intelligence.compareTo(other.intelligence) != 0) characteristics = characteristics.copy(intelligence = other.intelligence)
//    if (willpower.compareTo(other.willpower) != 0) characteristics = characteristics.copy(willpower = other.willpower)
//    if (fellowship.compareTo(other.fellowship) != 0) characteristics = characteristics.copy(fellowship = other.fellowship)
//
//    return characteristics
//}