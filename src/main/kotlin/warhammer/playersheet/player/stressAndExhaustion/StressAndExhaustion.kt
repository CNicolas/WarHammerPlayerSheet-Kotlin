package warhammer.playersheet.player.stressAndExhaustion

import warhammer.database.entities.player.Player
import warhammer.playersheet.player.stressAndExhaustion.ExhaustionState.*
import warhammer.playersheet.player.stressAndExhaustion.StressState.*

// region STRESS
fun Player.addStress(stressPoints: Int): Player =
        this.copy(
                state = state.copy(
                        stress = stress + stressPoints
                )
        )

fun Player.removeStress(stressPoints: Int): Player {
    val newStressValue = when {
        stress - stressPoints <= 0 -> 0
        else -> stress - stressPoints
    }
    return this.copy(state = state.copy(stress = newStressValue))
}

fun Player.stressState(): StressState =
        when {
            stress >= maxStress -> FAINTED
            stress >= maxStress / 2 -> STRESSED
            else -> NOT_STRESSED
        }
// endregion

// region EXHAUSTION
fun Player.addExhaustion(exhaustionPoints: Int): Player =
        this.copy(
                state = state.copy(
                        exhaustion = exhaustion + exhaustionPoints
                )
        )

fun Player.removeExhaustion(exhaustionPoints: Int): Player {
    val newExhaustionValue = when {
        exhaustion - exhaustionPoints <= 0 -> 0
        else -> exhaustion - exhaustionPoints
    }
    return this.copy(state = state.copy(exhaustion = newExhaustionValue))
}

fun Player.exhaustionState(): ExhaustionState =
        when {
            exhaustion >= maxExhaustion -> COMA
            exhaustion >= maxExhaustion / 2 -> EXHAUSTED
            else -> NOT_EXHAUSTED
        }

// endregion