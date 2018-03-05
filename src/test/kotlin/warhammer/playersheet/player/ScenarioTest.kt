package warhammer.playersheet.player

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.HandFacade
import warhammer.database.PlayerFacade
import warhammer.database.entities.hand.DifficultyLevel
import warhammer.database.entities.hand.Hand
import warhammer.database.entities.player.CharacteristicValue
import warhammer.database.entities.player.Player
import warhammer.database.entities.player.enums.Characteristic.AGILITY
import warhammer.database.entities.player.enums.Characteristic.FELLOWSHIP
import warhammer.database.entities.player.playerLinked.item.Weapon
import warhammer.database.entities.player.playerLinked.item.enums.Quality
import warhammer.database.entities.player.playerLinked.item.enums.Range
import warhammer.database.extensions.items.addItem
import warhammer.database.extensions.talents.addTalent
import warhammer.database.extensions.talents.equipTalent
import warhammer.database.staticData.getAllTalents
import warhammer.dicelauncher.launch.launch
import warhammer.dicelauncher.launch.launchForStatistics
import warhammer.playersheet.TEST_DATABASE_URL
import warhammer.playersheet.TEST_DRIVER
import warhammer.playersheet.extensions.createHand

class ScenarioTest {
    private val facade = PlayerFacade(TEST_DATABASE_URL, TEST_DRIVER)
    private val handFacade = HandFacade(TEST_DATABASE_URL, TEST_DRIVER)

    @BeforeMethod
    fun clearDatabase() {
        facade.deleteAll()
        handFacade.deleteAll()
    }

    @Test
    fun should_modify_a_player_and_launch_a_hand() {
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

        player.addItem(Weapon(name = "Arc sylvain",
                damage = 5,
                criticalLevel = 3,
                quality = Quality.SUPERIOR,
                encumbrance = 5,
                range = Range.LONG,
                isEquipped = true))

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

        val initiative = player.createHand(AGILITY, "Initiative").launch()
        assertThat(initiative.isSuccessful).isTrue()
        val impossible = player.createHand(FELLOWSHIP, "Impossible", DifficultyLevel.GODLIKE)
                .launchForStatistics(50)
        assertThat(impossible.successfulPercentage).isLessThan(70.0)

        val talent = getAllTalents().first { it.name == "Ascétisme" }
        player.addTalent(talent)
        player.equipTalent(talent)
        val updatedPlayer4 = facade.save(player)
        assertThat(updatedPlayer4.talents.size).isEqualTo(1)
        assertThat(updatedPlayer4.talents[0].isEquipped).isTrue()
    }

    @Test
    fun should_use_dice_launcher_alone() {
        val hand = Hand("default", 4, 1, 1, challengeDicesCount = 1)
        assertThat(hand.launchForStatistics(100).successfulPercentage).isGreaterThanOrEqualTo(50.0)

        hand.name = "Good charac"
        handFacade.save(hand)

        hand.misfortuneDicesCount = 2
        assertThat(hand.launchForStatistics(100).successfulPercentage).isGreaterThanOrEqualTo(20.0)
        hand.name = "Harder"
        handFacade.add(hand)

        val firstHand = handFacade.find("Good charac")
        assertThat(firstHand).isNotNull()
        assertThat(firstHand!!.characteristicDicesCount).isEqualTo(4)
        assertThat(firstHand.expertiseDicesCount).isEqualTo(1)
        assertThat(firstHand.fortuneDicesCount).isEqualTo(1)
        assertThat(firstHand.challengeDicesCount).isEqualTo(1)
        assertThat(firstHand.misfortuneDicesCount).isEqualTo(0)

        val secondHand = handFacade.find("Harder")
        assertThat(secondHand).isNotNull()
        assertThat(secondHand!!.characteristicDicesCount).isEqualTo(4)
        assertThat(secondHand.expertiseDicesCount).isEqualTo(1)
        assertThat(secondHand.fortuneDicesCount).isEqualTo(1)
        assertThat(secondHand.challengeDicesCount).isEqualTo(1)
        assertThat(secondHand.misfortuneDicesCount).isEqualTo(2)

        hand.expertiseDicesCount = 0

        val secondHand2 = handFacade.update(hand)
        assertThat(handFacade.update(hand)?.expertiseDicesCount).isEqualTo(0)
        assertThat(secondHand2).isNotNull()
        assertThat(secondHand2!!.name).isEqualTo("Harder")
        assertThat(secondHand2.characteristicDicesCount).isEqualTo(4)
        assertThat(secondHand2.expertiseDicesCount).isEqualTo(0)
        assertThat(secondHand2.fortuneDicesCount).isEqualTo(1)
        assertThat(secondHand2.challengeDicesCount).isEqualTo(1)
        assertThat(secondHand2.misfortuneDicesCount).isEqualTo(2)
    }
}