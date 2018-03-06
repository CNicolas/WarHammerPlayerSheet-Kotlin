package warhammer.playersheet.extensions

import warhammer.database.entities.player.playerLinked.talent.Talent
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.TALENT
import warhammer.database.entities.player.playerLinked.talent.TalentType
import warhammer.database.extensions.talents.findByType
import warhammer.database.staticData.getAllTalents

fun findTalents(text: String? = null,
                cooldown: TalentCooldown? = null,
                type: TalentType? = null) =
        getAllTalents().findTalents(text, cooldown, type)

fun List<Talent>.findTalents(text: String? = null,
                             cooldown: TalentCooldown? = null,
                             type: TalentType? = null): List<Talent> {
    var filteredTalents = toList()

    if (cooldown != null) {
        filteredTalents = findByCooldown(cooldown)
    }

    if (type != null) {
        filteredTalents = findByType(type)
    }

    if (text != null) {
        filteredTalents = filteredTalents.findByText(text)
    }

    return filteredTalents
}

fun List<Talent>.findByCooldown(talentCooldown: TalentCooldown) = when (talentCooldown) {
    PASSIVE -> filter { it.cooldown == PASSIVE }
    TALENT -> filter { it.cooldown == TALENT }
}

fun List<Talent>.findByText(text: String) =
        filter { it.name.contains(text, true) || it.description.contains(text, true) }
