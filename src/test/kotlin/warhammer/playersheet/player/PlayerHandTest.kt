package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.PlayerFacade
import warhammer.database.entities.hand.DifficultyLevel.HARD
import warhammer.database.entities.hand.DifficultyLevel.MEDIUM
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic
import warhammer.database.extensions.skills.getSkillByName
import warhammer.playersheet.TEST_DATABASE_URL
import warhammer.playersheet.TEST_DRIVER
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
        val facade = PlayerFacade(TEST_DATABASE_URL, TEST_DRIVER)
        val player = facade.save(Player("John"))
        player.strength = CharacteristicValue(4, 2)
        player.getSkillByName("capacité de combat")!!.level = 2
        val hand = player.createHand(player.getSkillByName("capacité de combat")!!, "HandName", HARD)

        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(4)
        assertThat(hand.fortuneDicesCount).isEqualTo(2)
        assertThat(hand.expertiseDicesCount).isEqualTo(2)
        assertThat(hand.challengeDicesCount).isEqualTo(HARD.ordinal)
    }

    @Test
    fun should_create_hand_from_specialized_skill() {
        val facade = PlayerFacade(TEST_DATABASE_URL, TEST_DRIVER)

        val player = facade.save(Player("John"))
        player.strength = CharacteristicValue(5, 2)

        val fightSkill = player.getSkillByName("capacité de combat")!!
        fightSkill.level = 2
        fightSkill.specializations[0].isSpecialized = true

        val hand = player.createHand(fightSkill, fightSkill.specializations[0], "HandName", MEDIUM)

        assertThat(hand.name).isEqualTo("HandName")
        assertThat(hand.characteristicDicesCount).isEqualTo(5)
        assertThat(hand.fortuneDicesCount).isEqualTo(player.strength.fortuneValue + 1)
        assertThat(hand.expertiseDicesCount).isEqualTo(fightSkill.level)
        assertThat(hand.challengeDicesCount).isEqualTo(MEDIUM.challengeDicesCount)
    }
}