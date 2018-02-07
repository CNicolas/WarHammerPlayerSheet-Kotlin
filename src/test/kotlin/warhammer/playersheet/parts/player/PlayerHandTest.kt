package warhammer.playersheet.parts.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.Hand
import warhammer.database.entities.Player
import warhammer.database.entities.characteristics.Characteristic
import warhammer.database.entities.characteristics.CharacteristicValue
import warhammer.database.entities.characteristics.PlayerCharacteristics
import warhammer.playersheet.createHand

class PlayerHandTest {
    @Test
    fun should_create_strength_hand_from_player() {
        val playerCharacteristics = PlayerCharacteristics(strengthValue = CharacteristicValue(3, 1))
        val player = Player("PlayerName", characteristics = playerCharacteristics)

        val hand: Hand = player.createHand(Characteristic.STRENGTH)
        assertThat(hand).isNotNull()
        assertThat(hand.name).isEqualTo("Hand")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }
}