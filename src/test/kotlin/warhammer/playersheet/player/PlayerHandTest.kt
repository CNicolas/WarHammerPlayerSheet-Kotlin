package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic
import warhammer.playersheet.extensions.createHand

class PlayerHandTest {
    @Test
    fun should_create_strength_hand_from_player() {
        val player = Player(name = "PlayerName", strength = CharacteristicValue(3, 1))

        val hand = player.createHand(Characteristic.STRENGTH, "Hand")
        assertThat(hand).isNotNull()
        assertThat(hand.name).isEqualTo("Hand")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }
}