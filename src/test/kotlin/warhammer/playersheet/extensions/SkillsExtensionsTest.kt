package warhammer.playersheet.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.enums.Characteristic.AGILITY
import warhammer.database.entities.player.enums.Characteristic.STRENGTH
import warhammer.database.staticData.getAllSkills

class SkillsExtensionsTest {
    @Test
    fun should_find_skill_by_extensions() {
        val strengthSkills = getAllSkills().findByCharacteristic(STRENGTH)
        assertThat(strengthSkills.size).isEqualTo(3)

        val fightSkill = strengthSkills.findByText("capacit")
        assertThat(fightSkill.size).isEqualTo(1)
        assertThat(fightSkill[0].name).isEqualTo("Capacité de Combat")

        val oneTimeFilteredSkills = findSkills("caPAcité", AGILITY)
        assertThat(oneTimeFilteredSkills.size).isEqualTo(1)
        assertThat(oneTimeFilteredSkills[0].name).isEqualTo("Capacité de Tir")
    }

    @Test
    fun should_all_skills() {
        val allSkills = findSkills()
        assertThat(allSkills.size).isEqualTo(27)
    }

    @Test
    fun should_not_find_skill() {
        val emptySkills = findSkills("nothing")
        assertThat(emptySkills).isEmpty()
    }
}