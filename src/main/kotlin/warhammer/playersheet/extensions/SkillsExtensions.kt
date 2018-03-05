package warhammer.playersheet.extensions

import warhammer.database.entities.player.enums.Characteristic
import warhammer.database.entities.player.playerLinked.skill.Skill
import warhammer.database.staticData.getAllSkills

fun findSkills(text: String? = null, characteristic: Characteristic? = null) =
        getAllSkills().findSkills(text, characteristic)

fun List<Skill>.findSkills(text: String? = null, characteristic: Characteristic? = null): List<Skill> {
    var filteredSkills = toList()

    if (characteristic != null) {
        filteredSkills = findByCharacteristic(characteristic)
    }

    if (text != null) {
        filteredSkills = filteredSkills.filter {
            it.name.contains(text, true)
        }
    }

    return filteredSkills
}

fun List<Skill>.findByCharacteristic(characteristic: Characteristic) =
        filter { it.characteristic == characteristic }
