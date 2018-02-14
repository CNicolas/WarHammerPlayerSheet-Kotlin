package warhammer.playersheet.player.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.Player

class PlayerManipulationTest {
    @Test
    fun should_earn_an_experience_point() {
        var player = Player("PlayerName")

        assertThat(player.totalExperience).isEqualTo(0)
        assertThat(player.availableExperience).isEqualTo(0)

        player = player.earnExperiencePoints(1)

        assertThat(player.totalExperience).isEqualTo(1)
        assertThat(player.availableExperience).isEqualTo(1)
    }
}