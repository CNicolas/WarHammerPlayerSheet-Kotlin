package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
import warhammer.database.entities.player.playerLinked.talent.TalentType.AFFINITY
import warhammer.database.extensions.talents.findByType
import warhammer.database.staticData.getAllTalents

class TalentsExtensionsTest {
    @Test
    fun should_find_talent_by_extensions() {
        val passiveTalents = getAllTalents().findByCooldown(PASSIVE)
        assertThat(passiveTalents.size).isEqualTo(84)

        val passiveAffinityTalents = passiveTalents.findByType(AFFINITY)
        assertThat(passiveAffinityTalents.size).isEqualTo(23)

        val passiveAffinityAetherTalents = passiveAffinityTalents.findTalents("aéthérique")
        assertThat(passiveAffinityAetherTalents.size).isEqualTo(2)

        val oneTimeFilteredTalents = findTalents("aéthérique", PASSIVE, AFFINITY)
        assertThat(oneTimeFilteredTalents.size).isEqualTo(2)
    }

    @Test
    fun should_find_talent_by_kotlin() {
        val passiveTalents = getAllTalents().filter { it.cooldown == PASSIVE }
        assertThat(passiveTalents.size).isEqualTo(84)

        val passiveAffinityTalents = passiveTalents.filter { it.type == AFFINITY }
        assertThat(passiveAffinityTalents.size).isEqualTo(23)

        val passiveAffinityAetherTalents = passiveAffinityTalents.filter {
            it.name.contains("aéthérique", true) ||
                    it.description.contains("aéthérique", true)
        }
        assertThat(passiveAffinityAetherTalents.size).isEqualTo(2)

        val oneTimeFilteredTalents = getAllTalents()
                .filter {
                    it.cooldown == PASSIVE &&
                            it.type == AFFINITY &&
                            it.name.contains("aéthérique", true) ||
                            it.description.contains("aéthérique", true)
                }
        assertThat(oneTimeFilteredTalents.size).isEqualTo(2)
    }
}