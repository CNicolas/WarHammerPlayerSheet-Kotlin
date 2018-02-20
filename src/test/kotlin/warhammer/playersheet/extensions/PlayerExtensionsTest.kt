package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.CharacteristicValue
import warhammer.database.entities.player.characteristics.PlayerCharacteristics
import warhammer.database.entities.player.other.Race

class PlayerExtensionsTest {
    @Test
    fun should_calculate_max_encumbrance() {
        val identicalCharacteristics = PlayerCharacteristics(strength = CharacteristicValue(3))

        val woodElf = Player(
                name = "WoodElf",
                race = Race.WOOD_ELF,
                characteristics = identicalCharacteristics
        ).setAutomaticFields()

        assertThat(woodElf.maxEncumbrance).isEqualTo(20)

        val dwarf = Player(
                name = "Dwarf",
                race = Race.DWARF,
                characteristics = identicalCharacteristics
        ).setAutomaticFields()

        assertThat(dwarf.maxEncumbrance).isEqualTo(25)
    }

    @Test
    fun should_max_encumbrance_equal_25_when_dwarf() {
        val player = Player(
                name = "PlayerName",
                race = Race.DWARF,
                characteristics = PlayerCharacteristics(strength = CharacteristicValue(3))
        ).setAutomaticFields()

        assertThat(player.maxEncumbrance).isEqualTo(25)
    }

    @Test
    fun should_max_stress_equals_twice_willpower() {
        val player = Player(
                name = "PlayerName",
                characteristics = PlayerCharacteristics(willpower = CharacteristicValue(2))
        ).setAutomaticFields()

        assertThat(player.maxStress).isEqualTo(4)
    }

    @Test
    fun should_max_exhaustion_equals_twice_toughness() {
        val player = Player(
                name = "PlayerName",
                characteristics = PlayerCharacteristics(toughness = CharacteristicValue(3))
        ).setAutomaticFields()

        assertThat(player.maxExhaustion).isEqualTo(6)
    }

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