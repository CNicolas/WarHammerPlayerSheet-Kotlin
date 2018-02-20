package warhammer.playersheet.extensions

import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.inventory.item.*
import warhammer.database.entities.player.inventory.item.enums.ItemType.*

// region Shortcuts on Player

fun Player.calculateEncumbrance(): Player {
    inventory.calculateEncumbrance()

    return this
}

fun Player.addItem(item: Item): Player {
    inventory.addItem(item)

    return this
}

fun Player.removeItemByName(name: String): Player {
    inventory.removeItemByName(name)

    return this
}

// endregion

fun PlayerInventory.calculateEncumbrance(): PlayerInventory {
    encumbrance = items.sumBy { it.encumbrance }

    return this
}

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
    items.add(item)

    return calculateEncumbrance()
}

fun PlayerInventory.removeItemByName(name: String): PlayerInventory {
    items.removeAt(items.indexOfFirst { it.name == name })

    return calculateEncumbrance()
}

fun PlayerInventory.updateItemByName(name: String, item: Item): PlayerInventory {
    items.forEach {
        if (name == it.name) {
            it.merge(item)
        }
    }

    return calculateEncumbrance()
}

private fun Item.merge(newItem: Item): Item {
    if (criticalLevel != newItem.criticalLevel) criticalLevel = newItem.criticalLevel
    if (damage != newItem.damage) damage = newItem.damage
    if (defense != newItem.defense) defense = newItem.defense
    if (description != newItem.description) description = newItem.description
    if (encumbrance != newItem.encumbrance) encumbrance = newItem.encumbrance
    if (isEquipped != newItem.isEquipped) isEquipped = newItem.isEquipped
    if (quality != newItem.quality) quality = newItem.quality
    if (quantity != newItem.quantity) quantity = newItem.quantity
    if (range != newItem.range) range = newItem.range
    if (soak != newItem.soak) soak = newItem.soak
    if (type != newItem.type) type = newItem.type
    if (uses != newItem.uses) uses = newItem.uses

    return this
}

internal const val ENCUMBRANCE_OVERLOAD_TO_MAX = 5