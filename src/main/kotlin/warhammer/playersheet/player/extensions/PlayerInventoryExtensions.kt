package warhammer.playersheet.player.extensions

import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.inventory.item.*
import warhammer.database.entities.player.inventory.item.enums.ItemType.*

fun Player.calculateEncumbrance(): Player = copy(
        inventory = inventory.copy(
                encumbrance = items.sumBy { it.encumbrance }
        )
)

fun PlayerInventory.findItemByName(name: String): Item? =
        items.find { it.name == name }

fun Player.addItem(item: Item): Player = copy(
        inventory = inventory.addItem(item)
).calculateEncumbrance()

fun PlayerInventory.addItem(item: Item): PlayerInventory {
    val newItems = items.toMutableList()
    newItems.add(item)

    return updateItems(newItems)
}

fun Player.removeItemByName(name: String): Player = copy(
        inventory = inventory.removeItemByName(name)
).calculateEncumbrance()

fun PlayerInventory.removeItemByName(name: String): PlayerInventory {
    val newItems = items.toMutableList()
    newItems.removeAt(newItems.indexOfFirst { it.name == name })

    return updateItems(newItems)
}

fun PlayerInventory.updateItems(newItems: List<Item>): PlayerInventory = copy(
        items = newItems.map {
            when (it.type) {
                ITEM -> it as GenericItem
                ARMOR -> it as Armor
                WEAPON -> it as Weapon
                EXPANDABLE -> it as Expandable
            }
        }
)