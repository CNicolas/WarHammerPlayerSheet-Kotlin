package warhammer.playersheet.player.extensions

import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.inventory.item.*
import warhammer.database.entities.player.inventory.item.enums.ItemType.*

// region Shortcuts on Player

fun Player.calculateEncumbrance(): Player = copy(inventory = inventory.calculateEncumbrance())

fun Player.addItem(item: Item): Player = copy(inventory = inventory.addItem(item)).calculateEncumbrance()

fun Player.removeItemByName(name: String): Player = copy(inventory = inventory.removeItemByName(name)).calculateEncumbrance()

// endregion

fun PlayerInventory.calculateEncumbrance(): PlayerInventory = copy(
        encumbrance = items.sumBy { it.encumbrance }
)

// region Find by name
fun PlayerInventory.findItemByName(name: String): Item? = items.find { it.name == name }

fun PlayerInventory.findArmorByName(name: String): Armor? = items
        .find { it.type == ARMOR && it.name == name } as? Armor?

fun PlayerInventory.findExpandableByName(name: String): Expandable? = items
        .find { it.type == EXPANDABLE && it.name == name } as? Expandable?

fun PlayerInventory.findGenericItemByName(name: String): GenericItem? = items
        .find { it.type == ITEM && it.name == name } as? GenericItem?

fun PlayerInventory.findWeaopnByName(name: String): Weapon? = items
        .find { it.type == WEAPON && it.name == name } as? Weapon?
// endregion

fun PlayerInventory.addItem(item: Item): PlayerInventory {
    val newItems = items.toMutableList()
    newItems.add(item)

    return updateItems(newItems)
}

fun PlayerInventory.removeItemByName(name: String): PlayerInventory {
    val newItems = items.toMutableList()
    newItems.removeAt(newItems.indexOfFirst { it.name == name })

    return updateItems(newItems)
}

fun PlayerInventory.updateItemByName(name: String, item: Item): PlayerInventory =
        updateItems(items.map {
            when (name) {
                it.name -> item.setIds(it)
                else -> it
            }
        })

fun PlayerInventory.updateItems(newItems: List<Item>): PlayerInventory = copy(
        items = newItems.map {
            when (it.type) {
                ITEM -> it as GenericItem
                ARMOR -> it as Armor
                WEAPON -> it as Weapon
                EXPANDABLE -> it as Expandable
            }
        }
).calculateEncumbrance()

private fun Item.setIds(item: Item): Item = setIds(item.id, item.inventoryId)

private fun Item.setIds(id: Int, inventoryId: Int): Item = when (type) {
    ITEM -> (this as GenericItem).copy(id = id, inventoryId = inventoryId)
    ARMOR -> (this as Armor).copy(id = id, inventoryId = inventoryId)
    WEAPON -> (this as Weapon).copy(id = id, inventoryId = inventoryId)
    EXPANDABLE -> (this as Expandable).copy(id = id, inventoryId = inventoryId)
}
