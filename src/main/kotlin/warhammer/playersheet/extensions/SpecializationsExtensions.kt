package warhammer.playersheet.extensions

import warhammer.database.entities.player.enums.Characteristic
import warhammer.database.entities.player.playerLinked.skill.Skill
import warhammer.database.entities.player.playerLinked.skill.Specialization
import warhammer.database.staticData.getAllSkills

fun findSpecializations(text: String? = null,
                        characteristic: Characteristic? = null,
                        skill: Skill? = null,
                        skillName: String? = null): Map<Skill, List<Specialization>> {
    var filteredSkills = getAllSkills()

    if (skill != null) {
        filteredSkills = filteredSkills.filter { it == skill }
    }
    if (characteristic != null) {
        filteredSkills = filteredSkills.findByCharacteristic(characteristic)
    }
    if (skillName != null) {
        filteredSkills = filteredSkills.findByText(skillName)
    }
    if (text != null) {
        return if (filteredSkills.any { it.specializations.findByText(text).isNotEmpty() }) {
            filteredSkills.map { filteredSkill ->
                filteredSkill to filteredSkill.specializations.findByText(text)
            }.toMap()
        } else {
            mapOf()
        }
    }

    return filteredSkills.map { filteredSkill ->
        filteredSkill to filteredSkill.specializations
    }.toMap()
}

fun Map<Skill, List<Specialization>>.findSkillByName(name: String) =
        this.keys.firstOrNull { it.name.contains(name, true) }

fun List<Specialization>.findByText(text: String) =
        filter { it.name.contains(text, true) }
