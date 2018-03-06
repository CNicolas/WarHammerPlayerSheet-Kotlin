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

    @Test
    fun should_all_specializations() {
        val allSpecializationsMap = findSpecializations()
        assertThat(allSpecializationsMap.size).isEqualTo(27)
        assertThat(allSpecializationsMap.flatMap { it.value }.size).isGreaterThanOrEqualTo(3 * 27)
    }

    @Test
    fun should_return_specializations_of_shooting_skill() {
        val shootingSkill = findSkills("Capacité de Tir")[0]
        val specializationsBySkillName = findSpecializations(skillName = "Capacité de Tir")
        val shootingSkillNameSpecializations = specializationsBySkillName[shootingSkill]!!
        assertThat(shootingSkillNameSpecializations.size).isEqualTo(4)
        assertThat(shootingSkillNameSpecializations[0].name).isEqualTo("Arcs")

        val specializationsBySkill = findSpecializations(skill = shootingSkill)
        val shootingSkillSpecializations = specializationsBySkill[shootingSkill]!!
        assertThat(shootingSkillSpecializations.size).isEqualTo(4)
        assertThat(shootingSkillSpecializations[0].name).isEqualTo("Arcs")
    }

    @Test
    fun should_not_find_specialization() {
        val emptyMap = findSpecializations("nothing")
        assertThat(emptyMap.size).isEqualTo(0)
        assertThat(emptyMap).isEmpty()
    }
}