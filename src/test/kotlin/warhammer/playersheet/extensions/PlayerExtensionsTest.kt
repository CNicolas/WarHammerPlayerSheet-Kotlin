package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.playerLinked.item.Armor
import warhammer.playersheet.enums.PlayerLivingState.*

class PlayerExtensionsTest {
    @Test
    fun should_earn_an_experience_point() {
        val player = Player(name = "PlayerName")

        assertThat(player.totalExperience).isEqualTo(0)
        assertThat(player.availableExperience).isEqualTo(0)

        player.earnExperiencePoints(1)

        assertThat(player.totalExperience).isEqualTo(1)
        assertThat(player.availableExperience).isEqualTo(1)
    }

    @Test
    fun should_receive_damage_taking_soak_and_toughness_in_count() {
        val player = Player("John", toughness = CharacteristicValue(2), items = listOf(Armor(soak = 1)))

        val (reducedDamage, _) = player.receiveDamage(10)
        assertThat(reducedDamage).isEqualTo(7)
        assertThat(player.wounds).isEqualTo(7)

        val (minimumDamage, _) = player.receiveDamage(1)
        assertThat(minimumDamage).isEqualTo(1)
        assertThat(player.wounds).isEqualTo(8)
    }

    @Test
    fun should_lose_health_and_have_good_state() {
        val player = Player("John", maxWounds = 3, toughness = CharacteristicValue(1))

        assertThat(player.livingState).isEqualTo(UNINJURED)
        assertThat(player.receiveDamage(2).second).isEqualTo(SLIGHTLY_INJURED)
        assertThat(player.receiveDamage(1).second).isEqualTo(HEAVILY_INJURED)
        assertThat(player.receiveDamage(1).second).isEqualTo(KO)
        assertThat(player.receiveDamage(1).second).isEqualTo(DEAD)
    }
}