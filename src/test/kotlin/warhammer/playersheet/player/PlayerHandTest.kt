package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.Hand
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.Characteristic.STRENGTH
import warhammer.database.entities.player.characteristics.CharacteristicValue
import warhammer.database.entities.player.characteristics.PlayerCharacteristics
import warhammer.playersheet.player.extensions.createHand

class PlayerHandTest {
    @Test
    fun should_create_strength_hand_from_player() {
        val playerCharacteristics = PlayerCharacteristics(strength = CharacteristicValue(3, 1))
        val player = Player(name = "PlayerName", characteristics = playerCharacteristics)

        val hand: Hand = player.createHand(STRENGTH, "Hand")
        assertThat(hand).isNotNull()
        assertThat(hand.name).isEqualTo("Hand")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }
}