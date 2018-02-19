package warhammer.playersheet.player.stressAndExhaustion

import warhammer.database.entities.player.Player
import warhammer.playersheet.player.stressAndExhaustion.ExhaustionState.*
import warhammer.playersheet.player.stressAndExhaustion.StressState.*

// region STRESS
fun Player.addStress(stressPoints: Int): Player {
    state.stress += stressPoints

    return this
}

fun Player.removeStress(stressPoints: Int): Player {
    val newStressValue = when {
        stress - stressPoints <= 0 -> 0
        else -> stress - stressPoints
    }
    state.stress = newStressValue

    return this
}

fun Player.stressState(): StressState =
        when {
            stress >= maxStress -> FAINTED
            stress >= maxStress / 2 -> STRESSED
            else -> NOT_STRESSED
        }
// endregion

// region EXHAUSTION
fun Player.addExhaustion(exhaustionPoints: Int): Player {
    state.exhaustion += exhaustionPoints

    return this
}

fun Player.removeExhaustion(exhaustionPoints: Int): Player {
    val newExhaustionValue = when {
        exhaustion - exhaustionPoints <= 0 -> 0
        else -> exhaustion - exhaustionPoints
    }
    state.exhaustion = newExhaustionValue

    return this
}

fun Player.exhaustionState(): ExhaustionState =
        when {
            exhaustion >= maxExhaustion -> COMA
            exhaustion >= maxExhaustion / 2 -> EXHAUSTED
            else -> NOT_EXHAUSTED
        }

// endregion