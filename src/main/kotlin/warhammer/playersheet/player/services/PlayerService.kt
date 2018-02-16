package warhammer.playersheet.player.services

import warhammer.database.entities.player.Player
import warhammer.database.entities.player.PlayerInventory
import warhammer.database.entities.player.PlayerState
import warhammer.database.entities.player.characteristics.PlayerCharacteristics
import warhammer.database.services.PlayersDatabaseService

class PlayerService(private val playersDatabaseService: PlayersDatabaseService) {
    fun add(player: Player) = playersDatabaseService.add(player)

    // region FIND

    fun find(id: Int): Player? = playersDatabaseService.findById(id)

    fun find(name: String): Player? = playersDatabaseService.findByName(name)

    fun find(player: Player): Player? =
            playersDatabaseService.findById(player.id) ?: playersDatabaseService.findByName(player.name)

    fun findAll(): List<Player> = playersDatabaseService.findAll()

    // endregion

    // region UPDATE

    fun update(player: Player): Player? =
            playersDatabaseService.update(player)

    fun update(player: Player, playerState: PlayerState): Player? =
            playersDatabaseService.update(player.copy(state = playerState))

    fun update(player: Player, playerCharacteristics: PlayerCharacteristics): Player? =
            playersDatabaseService.update(player.copy(characteristics = playerCharacteristics))

    fun update(player: Player, playerInventory: PlayerInventory): Player? =
            playersDatabaseService.update(player.copy(inventory = playerInventory))

    // endregion

    // region DELETE

    fun delete(player: Player) = playersDatabaseService.delete(player)

    fun deleteAll() = playersDatabaseService.deleteAll()

    // endregion
}