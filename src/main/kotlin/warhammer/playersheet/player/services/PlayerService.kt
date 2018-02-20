package warhammer.playersheet.player.services

import warhammer.database.entities.player.Player
import warhammer.database.services.PlayersDatabaseService
import warhammer.playersheet.player.extensions.setAutomaticFields

class PlayerService(private val playersDatabaseService: PlayersDatabaseService) {
    fun add(player: Player) = playersDatabaseService.add(player)

    // region FIND

    fun find(id: Int): Player? = playersDatabaseService.findById(id)

    fun find(name: String): Player? = playersDatabaseService.findByName(name)

    fun find(id: Int, name: String): Player? {
        val playerById = playersDatabaseService.findById(id)
        val playerByName = playersDatabaseService.findByName(name)

        return when {
            playerById?.id == playerByName?.id && playerById?.name == playerByName?.name -> playerById
            else -> null
        }
    }

    fun findAll(): List<Player> = playersDatabaseService.findAll()

    // endregion

    // region UPDATE

    fun update(player: Player): Player? = playersDatabaseService.update(player.setAutomaticFields())

    // endregion

    // region DELETE

    fun delete(player: Player) = playersDatabaseService.delete(player)

    fun deleteAll() = playersDatabaseService.deleteAll()

    // endregion
}