package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.PlayerState
import warhammer.database.entities.player.characteristics.CharacteristicValue
import warhammer.database.entities.player.characteristics.PlayerCharacteristics
import warhammer.database.entities.player.inventory.Money
import warhammer.database.entities.player.inventory.item.Weapon
import warhammer.database.entities.player.inventory.item.enums.Quality
import warhammer.database.entities.player.state.Career
import warhammer.database.entities.player.state.Stance
import warhammer.database.services.PlayersDatabaseService
import warhammer.playersheet.PlayerSheetContext
import warhammer.playersheet.player.extensions.findItemByName
import warhammer.playersheet.player.extensions.setAutomaticFields
import warhammer.playersheet.player.extensions.updateItemByName
import warhammer.playersheet.player.services.PlayerService

class PlayerServiceTest {
    private val playerService = PlayerService(PlayersDatabaseService(PlayerSheetContext.DATABASE_URL, PlayerSheetContext.DRIVER))

    @BeforeMethod
    fun clearDatabase() {
        playerService.deleteAll()
    }

    @Test
    fun should_find_by_different_means() {
        val id = 1
        val name = "John"
        val player = Player(name = name)
        playerService.add(player)

        val playerFoundById = playerService.find(id)
        assertThat(playerFoundById).isNotNull()
        assertThat(playerFoundById!!.id).isEqualTo(id)
        assertThat(playerFoundById.name).isEqualTo(name)

        val playerFoundByName = playerService.find(name)
        assertThat(playerFoundByName).isNotNull()
        assertThat(playerFoundByName!!.id).isEqualTo(id)
        assertThat(playerFoundByName.name).isEqualTo(name)

        val confusedPlayer1 = playerService.find(id = 1, name = "Mika")
        assertThat(confusedPlayer1).isNull()
        val confusedPlayer2 = playerService.find(id = 3, name = name)
        assertThat(confusedPlayer2).isNull()
    }

    @Test
    fun should_update_different_parts_of_player() {
        val characteristics = PlayerCharacteristics(strength = CharacteristicValue(2))
        val state = PlayerState(maxWounds = 10)
        val inventory = PlayerInventory()
        val player = Player(name = "John", characteristics = characteristics, state = state, inventory = inventory)
        val addedPlayer = playerService.add(player)

        assertThat(addedPlayer).isNotNull()
        assertThat(addedPlayer!!.name).isEqualTo("John")
        assertThat(addedPlayer.strength.value).isEqualTo(2)
        assertThat(addedPlayer.maxWounds).isEqualTo(10)
        assertThat(addedPlayer.maxEncumbrance).isEqualTo(0)

        addedPlayer.name = "Jack"
        addedPlayer.maxWounds = 12

        val updateByPlayer = playerService.update(addedPlayer.copy(name = "Jack", state = addedPlayer.state.copy(maxWounds = 12)))
        assertThat(updateByPlayer).isNotNull()
        assertThat(updateByPlayer!!.name).isEqualTo("Jack")
        assertThat(updateByPlayer.strength.value).isEqualTo(2)
        assertThat(updateByPlayer.maxWounds).isEqualTo(12)
        assertThat(updateByPlayer.maxEncumbrance).isEqualTo(15)
    }

    @Test
    fun should_update_with_new_parts_of_player() {
        val addedPlayer = playerService.add(Player(name = "John"))
        assertThat(addedPlayer).isNotNull()
        assertThat(addedPlayer!!.name).isEqualTo("John")
        assertThat(addedPlayer.willpower.value).isEqualTo(0)
        assertThat(addedPlayer.willpower.fortuneValue).isEqualTo(0)
        assertThat(addedPlayer.careerName).isEqualTo("Unemployed")
        assertThat(addedPlayer.money.brass).isEqualTo(0)

        val updateByPlayer = playerService.update(Player(id = 1, name = "Jack"))
        assertThat(updateByPlayer).isNotNull()
        assertThat(updateByPlayer!!.name).isEqualTo("Jack")
        assertThat(updateByPlayer.willpower.value).isEqualTo(0)
        assertThat(updateByPlayer.willpower.fortuneValue).isEqualTo(0)
        assertThat(updateByPlayer.careerName).isEqualTo("Unemployed")
        assertThat(updateByPlayer.money.brass).isEqualTo(0)
    }

    @Test
    fun should_crud_player_in_database() {
        val strengthValue = CharacteristicValue(3, 1)
        val fellowShipValue = CharacteristicValue(2)
        val intelligenceValue = CharacteristicValue(3, 2)

        val stance = Stance(maxReckless = 3)
        val career = Career(name = "Wizard", rank = 2)

        val weapon = Weapon(name = "Sword", encumbrance = 3, damage = 4, criticalLevel = 3, quality = Quality.NORMAL)
        val money = Money(1, 2, 3)

        val player = Player(
                name = "PlayerName",
                characteristics = PlayerCharacteristics(
                        strength = strengthValue,
                        fellowship = fellowShipValue,
                        intelligence = intelligenceValue),
                state = PlayerState(
                        maxStress = 6,
                        stance = stance,
                        career = career
                ),
                inventory = PlayerInventory(
                        money = money,
                        items = mutableListOf(weapon)
                )
        ).setAutomaticFields()

        val savedPlayer = playerService.add(player)
        assertThat(savedPlayer).isNotNull()
        assertThat(savedPlayer!!.name).isEqualTo("PlayerName")
        assertThat(savedPlayer.strength).isEqualToComparingFieldByField(strengthValue)
        assertThat(savedPlayer.fellowship).isEqualToComparingFieldByField(fellowShipValue)
        assertThat(savedPlayer.intelligence).isEqualToComparingFieldByField(intelligenceValue)
        assertThat(savedPlayer.maxReckless).isEqualTo(3)
        assertThat(savedPlayer.careerName).isEqualTo("Wizard")
        assertThat(savedPlayer.rank).isEqualTo(2)
        assertThat(savedPlayer.money.gold).isEqualTo(3)
        assertThat(savedPlayer.inventory.findItemByName("Sword")?.damage).isEqualTo(4)

        val updatePlayerCharacteristics = savedPlayer.characteristics
        updatePlayerCharacteristics.intelligence = CharacteristicValue(0)

        val updatePlayerInventory = savedPlayer.inventory
        updatePlayerInventory.money.gold = 2
        updatePlayerInventory.updateItemByName(weapon.name, weapon.copy(damage = 5))

        val updatePlayerState = savedPlayer.state
        updatePlayerState.maxReckless = 2
        updatePlayerState.rank = 3

        val updatedPlayer = playerService.update(savedPlayer.copy(
                characteristics = updatePlayerCharacteristics,
                state = updatePlayerState,
                inventory = updatePlayerInventory
        ))
        assertThat(updatedPlayer).isNotNull()
        assertThat(updatedPlayer!!.name).isEqualTo("PlayerName")
        assertThat(updatedPlayer.strength).isEqualToComparingFieldByField(strengthValue)
        assertThat(updatedPlayer.fellowship).isEqualToComparingFieldByField(fellowShipValue)
        assertThat(updatedPlayer.intelligence).isEqualToComparingFieldByField(CharacteristicValue(0))
        assertThat(updatedPlayer.maxReckless).isEqualTo(2)
        assertThat(updatedPlayer.rank).isEqualTo(3)
        assertThat(updatedPlayer.money.gold).isEqualTo(2)
        assertThat(updatedPlayer.inventory.findItemByName("Sword")?.damage).isEqualTo(5)

        val isDeleted = playerService.delete(updatedPlayer)
        assertThat(isDeleted).isTrue()
        val nullPlayer = playerService.find(updatedPlayer.id)
        assertThat(nullPlayer).isNull()
        assertThat(playerService.findAll().size).isEqualTo(0)

        val cantDelete = playerService.delete(updatedPlayer)
        assertThat(cantDelete).isFalse()
    }
}