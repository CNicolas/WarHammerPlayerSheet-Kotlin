package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.PlayerFacade
import warhammer.database.entities.hand.DifficultyLevel
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.extensions.addItem
import warhammer.database.entities.player.playerLinked.item.Weapon
import warhammer.database.entities.player.playerLinked.item.enums.Quality
import warhammer.database.entities.player.playerLinked.item.enums.Range
import warhammer.dicelauncher.launch.launch
import warhammer.dicelauncher.launch.launchForStatistics
import warhammer.playersheet.TEST_DATABASE_URL
import warhammer.playersheet.TEST_DRIVER

class ScenarioTest {
    private val facade = PlayerFacade(TEST_DATABASE_URL, TEST_DRIVER)

    @BeforeMethod
    fun clearDatabase() {
        facade.deleteAll()
    }

    @Test
    fun should_be_used() {
        val player = facade.save(Player(name = "John"))
        assertThat(player).isNotNull()

        player.careerName = "Assassin"
        player.rank = 3
        player.strength = CharacteristicValue(3)
        player.toughness = CharacteristicValue(4)
        player.agility = CharacteristicValue(5, 2)
        player.intelligence = CharacteristicValue(3)
        player.willpower = CharacteristicValue(4)
        player.fellowship = CharacteristicValue(2)

        val updatedPlayer1 = facade.save(player)
        assertThat(updatedPlayer1.name).isEqualTo("John")
        assertThat(updatedPlayer1.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer1.rank).isEqualTo(3)
        assertThat(updatedPlayer1.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer1.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer1.agility).isEqualTo(CharacteristicValue(5, 2))
        assertThat(updatedPlayer1.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer1.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer1.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer1.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer1.maxStress).isEqualTo(8)
        assertThat(updatedPlayer1.encumbrance).isEqualTo(0)

        player.addItem(Weapon(name = "Arc sylvain", damage = 5, criticalLevel = 3, quality = Quality.SUPERIOR, encumbrance = 5, range = Range.LONG, isEquipped = true))

        val updatedPlayer2 = facade.save(player)
        assertThat(updatedPlayer2.name).isEqualTo("John")
        assertThat(updatedPlayer2.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer2.rank).isEqualTo(3)
        assertThat(updatedPlayer2.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer2.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer2.agility).isEqualTo(CharacteristicValue(5, 2))
        assertThat(updatedPlayer2.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer2.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer2.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer2.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer2.maxStress).isEqualTo(8)
        assertThat(updatedPlayer2.encumbrance).isEqualTo(5)

        player.agility = player.agility.copy(fortuneValue = player.agility.fortuneValue + 1)

        val updatedPlayer3 = facade.save(player)
        assertThat(updatedPlayer3.name).isEqualTo("John")
        assertThat(updatedPlayer3.careerName).isEqualTo("Assassin")
        assertThat(updatedPlayer3.rank).isEqualTo(3)
        assertThat(updatedPlayer3.strength).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer3.toughness).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer3.agility).isEqualTo(CharacteristicValue(5, 3))
        assertThat(updatedPlayer3.intelligence).isEqualTo(CharacteristicValue(3))
        assertThat(updatedPlayer3.willpower).isEqualTo(CharacteristicValue(4))
        assertThat(updatedPlayer3.fellowship).isEqualTo(CharacteristicValue(2))
        assertThat(updatedPlayer3.maxEncumbrance).isEqualTo(20)
        assertThat(updatedPlayer3.maxStress).isEqualTo(8)
        assertThat(updatedPlayer3.encumbrance).isEqualTo(5)

        val initiative = player.agility.getHand("Initiative").launch()
        assertThat(initiative.isSuccessful).isTrue()
        val impossible = player.fellowship.getHand("Impossible", DifficultyLevel.GODLIKE).launchForStatistics(50)
        assertThat(impossible.successfulPercentage).isLessThan(70.0)
    }
}