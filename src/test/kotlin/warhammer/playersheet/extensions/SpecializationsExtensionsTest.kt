package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.enums.Characteristic

class SpecializationsExtensionsTest {
    @Test
    fun should_find_sspecialization_by_extensions() {
        val oneTimeFilteredSpecializations = findSpecializations("ation", Characteristic.TOUGHNESS)
        assertThat(oneTimeFilteredSpecializations.size).isEqualTo(1)
        val skill = oneTimeFilteredSpecializations.findSkillByName("Résistance")
        assertThat(skill).isNotNull()
        assertThat(oneTimeFilteredSpecializations[skill]!!.size).isEqualTo(1)
        assertThat(oneTimeFilteredSpecializations[skill]!![0].name).isEqualTo("Récupération Après l'Effort")
    }
}