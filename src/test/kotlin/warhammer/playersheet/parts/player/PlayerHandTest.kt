package warhammer.playersheet.parts.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.dicelauncher.hand.Hand
import warhammer.playersheet.parts.characteristics.Characteristic
import warhammer.playersheet.parts.characteristics.CharacteristicValue
import warhammer.playersheet.parts.characteristics.PlayerCharacteristics

class PlayerHandTest {
    @Test
    fun should_create_strength_hand_from_player() {
        val playerCharacteristics = PlayerCharacteristics(strengthValue = CharacteristicValue(3, 1))
        val player = Player("PlayerName", characteristics = playerCharacteristics)

        val hand: Hand = player.createHand("HandName", Characteristic.STRENGTH)
        assertThat(hand).isNotNull()
        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }
}