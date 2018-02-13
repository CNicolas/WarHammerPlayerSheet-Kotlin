package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.characteristics.Characteristic.*
import warhammer.database.entities.player.characteristics.CharacteristicValue
import warhammer.database.entities.player.characteristics.PlayerCharacteristics
import warhammer.database.services.PlayersDatabaseService
import warhammer.playersheet.PlayerSheetContext

class PlayerDatabaseTest {
    private val playersDatabaseService = PlayersDatabaseService(PlayerSheetContext.DATABASE_URL, PlayerSheetContext.DRIVER)

    @BeforeMethod
    fun clearDatabase() {
        playersDatabaseService.deleteAll()
    }

    @Test
    fun should_crud_player_in_database() {
        val strengthValue = CharacteristicValue(3, 1)
        val fellowShipValue = CharacteristicValue(2)
        val intelligenceValue = CharacteristicValue(3, 2)

        val playerCharacteristics = PlayerCharacteristics(strengthValue = strengthValue, fellowShipValue = fellowShipValue)
        val player = Player("PlayerName", characteristics = playerCharacteristics)

        playersDatabaseService.deleteAll()

        val savedPlayer = playersDatabaseService.add(player)
        assertThat(savedPlayer).isNotNull()
        assertThat(savedPlayer?.name).isEqualTo("PlayerName")
        assertThat(savedPlayer?.characteristics!![STRENGTH]).isEqualToComparingFieldByField(strengthValue)
        assertThat(savedPlayer.characteristics[FELLOWSHIP]).isEqualToComparingFieldByField(fellowShipValue)
        assertThat(savedPlayer.characteristics[INTELLIGENCE]).isEqualToComparingFieldByField(CharacteristicValue(0))

        val updatePlayerCharacteristics = PlayerCharacteristics(strengthValue = strengthValue,
                fellowShipValue = fellowShipValue,
                intelligenceValue = intelligenceValue)
        val updatedPlayer = playersDatabaseService.update(savedPlayer.copy(characteristics = updatePlayerCharacteristics))
        assertThat(updatedPlayer).isNotNull()
        assertThat(updatedPlayer?.name).isEqualTo("PlayerName")
        assertThat(updatedPlayer?.characteristics!![STRENGTH]).isEqualToComparingFieldByField(strengthValue)
        assertThat(updatedPlayer.characteristics[FELLOWSHIP]).isEqualToComparingFieldByField(fellowShipValue)
        assertThat(updatedPlayer.characteristics[INTELLIGENCE]).isEqualToComparingFieldByField(intelligenceValue)

        val isDeleted = playersDatabaseService.delete(updatedPlayer)
        assertThat(isDeleted).isTrue()
        val nullPlayer = playersDatabaseService.findById(updatedPlayer.id)
        assertThat(nullPlayer).isNull()
        assertThat(playersDatabaseService.findAll().size).isEqualTo(0)

        val cantDelete = playersDatabaseService.delete(updatedPlayer)
        assertThat(cantDelete).isFalse()
    }
}