package warhammer.playersheet

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import warhammer.database.entities.Hand
import warhammer.database.services.HandsDatabaseService
import warhammer.dicelauncher.launch.launchForStatistics
import warhammer.dicelauncher.launch.launchHand

class ArtifactsValidationTest {
    private val handsDatabaseService = HandsDatabaseService(PlayerSheetContext.DATABASE_URL, PlayerSheetContext.DRIVER)

    @BeforeMethod
    fun clearDatabase() {
        handsDatabaseService.deleteAll()
    }

    @Test
    fun should_launch_a_saved_hand() {
        val launchCount = 100
        val handName = "NiceOne"
        val hand = Hand(name = handName, characteristicDicesCount = 6, expertiseDicesCount = 6, fortuneDicesCount = 6)

        assertThat(launchHand(hand).faces.size).isGreaterThanOrEqualTo(18)

        handsDatabaseService.deleteAll()

        val savedHand = handsDatabaseService.add(hand)
        assertThat(savedHand).isNotNull()

        val statisticsFor100SavedHand = launchForStatistics(savedHand!!, launchCount)
        assertThat(statisticsFor100SavedHand.successfulLaunchCount).isLessThanOrEqualTo(launchCount)
        assertThat(statisticsFor100SavedHand.averageSuccess).isGreaterThanOrEqualTo(1.0)

        val foundHand = handsDatabaseService.findByName(handName)
        assertThat(foundHand).isNotNull()

        val statisticsFor100FoundHand = launchForStatistics(foundHand!!, launchCount)
        assertThat(statisticsFor100FoundHand.successfulLaunchCount).isLessThanOrEqualTo(launchCount)
        assertThat(statisticsFor100FoundHand.averageSuccess).isGreaterThanOrEqualTo(1.0)
    }
}