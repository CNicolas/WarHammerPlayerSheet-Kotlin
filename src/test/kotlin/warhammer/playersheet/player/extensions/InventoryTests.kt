package warhammer.playersheet.player.extensions

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.inventory.item.Armor
import warhammer.database.entities.player.inventory.item.Expandable
import warhammer.database.entities.player.inventory.item.GenericItem
import warhammer.database.entities.player.inventory.item.Weapon
import warhammer.database.entities.player.inventory.item.enums.ItemType
import warhammer.database.entities.player.inventory.item.enums.Quality.LOW

class InventoryTests {
    @Test
    fun should_have_encumbrance_of_2() {
        val player = Player(name = "PlayerName",
                inventory = PlayerInventory(
                        items = listOf(Weapon(name = "Baton", encumbrance = 2))
                )).setAutomaticFields()

        assertThat(player.encumbrance).isEqualTo(2)
    }

    @Test
    fun should_have_encumbrance_increased_when_adding_item() {
        val player = Player(name = "PlayerName",
                inventory = PlayerInventory(
                        items = listOf(Weapon(name = "Baton", encumbrance = 2))
                )).setAutomaticFields()

        assertThat(player.items.size).isEqualTo(1)
        assertThat(player.encumbrance).isEqualTo(2)
        assertThat(player.inventory.findItemByName("Baton")).isNotNull()
        assertThat(player.inventory.findItemByName("Baton")?.type).isEqualTo(ItemType.WEAPON)

        val newPlayer = player.addItem(Expandable(name = "Potion", quantity = 1, uses = 2, encumbrance = 1))
        assertThat(newPlayer.items.size).isEqualTo(2)
        assertThat(newPlayer.encumbrance).isEqualTo(3)
        assertThat(newPlayer.inventory.findItemByName("Baton")).isNotNull()
        assertThat(newPlayer.inventory.findItemByName("Baton")?.type).isEqualTo(ItemType.WEAPON)
        assertThat(newPlayer.inventory.findItemByName("Potion")).isNotNull()
        assertThat(newPlayer.inventory.findItemByName("Potion")?.type).isEqualTo(ItemType.EXPANDABLE)
    }

    @Test
    fun should_have_encumbrance_decreased_when_removing_item() {
        val player = Player(name = "PlayerName",
                inventory = PlayerInventory(
                        items = listOf(Weapon(name = "Baton", encumbrance = 2),
                                Armor(name = "Chapeau", encumbrance = 1))
                )).setAutomaticFields()

        assertThat(player.items.size).isEqualTo(2)
        assertThat(player.encumbrance).isEqualTo(3)
        assertThat(player.inventory.findItemByName("Baton")).isNotNull()
        assertThat(player.inventory.findItemByName("Baton")?.type).isEqualTo(ItemType.WEAPON)
        assertThat(player.inventory.findItemByName("Chapeau")).isNotNull()
        assertThat(player.inventory.findItemByName("Chapeau")?.type).isEqualTo(ItemType.ARMOR)

        val newPlayer = player.removeItemByName("Chapeau")
        assertThat(newPlayer.items.size).isEqualTo(1)
        assertThat(newPlayer.encumbrance).isEqualTo(2)
        assertThat(newPlayer.inventory.findItemByName("Baton")).isNotNull()
        assertThat(newPlayer.inventory.findItemByName("Baton")?.type).isEqualTo(ItemType.WEAPON)
        assertThat(newPlayer.inventory.findItemByName("Chapeau")).isNull()
    }

    @Test
    fun should_get_typed_item_by_name() {
        val items = listOf(
                Armor(name = "Combinaison", soak = 2),
                Expandable(name = "Compote", uses = 3),
                GenericItem(name = "Corde", quality = LOW),
                Weapon(name = "Pince", damage = 2)
        )
        val player = Player(name = "PlayerName", inventory = PlayerInventory(items = items)).setAutomaticFields()

        val armor = player.inventory.findArmorByName("Combinaison")
        assertThat(armor).isNotNull()
        assertThat(armor!!.soak).isEqualTo(2)

        val expandable = player.inventory.findExpandableByName("Compote")
        assertThat(expandable).isNotNull()
        assertThat(expandable!!.uses).isEqualTo(3)

        val genericItem = player.inventory.findGenericItemByName("Corde")
        assertThat(genericItem).isNotNull()
        assertThat(genericItem!!.quality).isEqualTo(LOW)

        val weapon = player.inventory.findWeaopnByName("Pince")
        assertThat(weapon).isNotNull()
        assertThat(weapon!!.damage).isEqualTo(2)
    }
}