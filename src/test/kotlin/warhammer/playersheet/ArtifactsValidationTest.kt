package warhammer.playersheet

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.HandFacade
import warhammer.database.entities.hand.Hand
import warhammer.dicelauncher.launch.launchForStatistics
import warhammer.dicelauncher.launch.launchHand
import warhammer.dicelauncher.launch.launchHandForStatistics

class ArtifactsValidationTest {
    private val handFacade = HandFacade(TEST_DATABASE_URL, TEST_DRIVER)

    @BeforeMethod
    fun clearDatabase() {
        handFacade.deleteAll()
    }

    @Test
    fun should_launch_a_saved_hand() {
        val launchCount = 100
        val handName = "NiceOne"
        val hand = Hand(name = handName, characteristicDicesCount = 6, expertiseDicesCount = 6, fortuneDicesCount = 6)

        assertThat(launchHand(hand).faces.size).isGreaterThanOrEqualTo(18)

        handFacade.deleteAll()

        val savedHand = handFacade.save(hand)
        assertThat(savedHand).isNotNull()

        val statisticsFor100SavedHand = savedHand.launchForStatistics(launchCount)
        assertThat(statisticsFor100SavedHand.successfulLaunchCount).isLessThanOrEqualTo(launchCount)
        assertThat(statisticsFor100SavedHand.averageSuccess).isGreaterThanOrEqualTo(1.0)

        val foundHand = handFacade.find(handName)
        assertThat(foundHand).isNotNull()

        val statisticsFor100FoundHand = launchHandForStatistics(foundHand!!, launchCount)
        assertThat(statisticsFor100FoundHand.successfulLaunchCount).isLessThanOrEqualTo(launchCount)
        assertThat(statisticsFor100FoundHand.averageSuccess).isGreaterThanOrEqualTo(1.0)
    }

    @Test
    fun should_validate_context_values() {
        assertThat(TEST_DATABASE_URL).isEqualTo("jdbc:sqlite:testSqlite:?mode=memory&cache=shared")
        assertThat(TEST_DRIVER).isEqualTo("org.sqlite.JDBC")
    }
}