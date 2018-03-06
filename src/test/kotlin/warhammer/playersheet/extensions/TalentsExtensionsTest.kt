package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.TALENT
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

        val passiveAffinityAetherTalents = passiveAffinityTalents.findByText("aéthérique")
        assertThat(passiveAffinityAetherTalents.size).isEqualTo(2)

        val oneTimeFilteredTalents = findTalents("aéthérique", PASSIVE, AFFINITY)
        assertThat(oneTimeFilteredTalents.size).isEqualTo(2)
    }

    @Test
    fun should_find_talents_with_cooldown() {
        val exhaustibleTalents = getAllTalents().findByCooldown(TALENT)
        assertThat(exhaustibleTalents.size).isEqualTo(22)
    }

    @Test
    fun should_all_talents() {
        val allTalents = findTalents()
        assertThat(allTalents.size).isEqualTo(106)
    }

    @Test
    fun should_not_find_talent() {
        val emptyTalents = findTalents("nothing")
        assertThat(emptyTalents).isEmpty()
    }
}