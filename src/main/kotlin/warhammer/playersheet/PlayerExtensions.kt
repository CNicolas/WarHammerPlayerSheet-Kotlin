package warhammer.playersheet

import warhammer.database.entities.Hand
import warhammer.database.entities.Player
import warhammer.database.entities.characteristics.Characteristic

fun Player.createHand(characteristic: Characteristic, name: String = "Hand"): Hand = characteristics.getHand(characteristic, name)