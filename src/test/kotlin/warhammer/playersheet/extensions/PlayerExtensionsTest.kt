package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.Player

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
}