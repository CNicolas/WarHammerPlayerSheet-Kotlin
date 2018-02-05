package warhammer.playersheet.parts.characteristics

import warhammer.dicelauncher.hand.Hand

data class CharacteristicValue(val value: Int, val fortuneValue: Int = 0) {
    fun getHand(name: String) = Hand(name, characteristicDicesCount = value, fortuneDicesCount = fortuneValue)
}