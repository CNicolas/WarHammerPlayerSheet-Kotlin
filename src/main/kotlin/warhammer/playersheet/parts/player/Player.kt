package warhammer.playersheet.parts.player

import warhammer.dicelauncher.hand.Hand
import warhammer.playersheet.parts.characteristics.Characteristic
import warhammer.playersheet.parts.characteristics.PlayerCharacteristics

data class Player(val name: String,
                  val id: Int = -1,
                  val characteristics: PlayerCharacteristics = PlayerCharacteristics()) {
    fun createHand(name: String, characteristic: Characteristic): Hand = characteristics[characteristic].getHand(name)

}