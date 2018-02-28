package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.PlayerFacade
import warhammer.database.entities.hand.DifficultyLevel.HARD
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic
import warhammer.database.entities.player.extensions.getSkillByName
import warhammer.playersheet.PlayerSheetContext
import warhammer.playersheet.extensions.createHand

class PlayerHandTest {
    @Test
    fun should_create_strength_hand_from_player() {
        val player = Player(name = "PlayerName", strength = CharacteristicValue(3, 1))

        val hand = player.createHand(Characteristic.STRENGTH, "Hand")
        assertThat(hand.name).isEqualTo("Hand")
        assertThat(hand.characteristicDicesCount).isEqualTo(3)
        assertThat(hand.fortuneDicesCount).isEqualTo(1)
    }

    @Test
    fun should_create_hand_from_skill() {
        val facade = PlayerFacade(PlayerSheetContext.DATABASE_URL, PlayerSheetContext.DRIVER)
        val player = facade.save(Player("John"))
        player.strength = CharacteristicValue(4,2)
        player.getSkillByName("capacité de combat")!!.level = 2
        val hand = player.createHand(player.getSkillByName("capacité de combat")!!, "HandName", HARD)

        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(4)
        assertThat(hand.fortuneDicesCount).isEqualTo(2)
        assertThat(hand.expertiseDicesCount).isEqualTo(2)
        assertThat(hand.challengeDicesCount).isEqualTo(HARD.ordinal)
    }
}