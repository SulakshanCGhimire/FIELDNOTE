package np.com.sulakshan.fieldnote.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LocationTest {

    @Test
    fun validCoordinates_areAccepted() {
        val location = Location(latitude = 28.2096, longitude = 83.9856)
        assertEquals(28.2096, location.latitude, 0.0)
    }

    @Test
    fun latitudeAbove90_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            Location(latitude = 95.0, longitude = 10.0)
        }
    }

    @Test
    fun negativeAccuracy_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            Location(latitude = 10.0, longitude = 10.0, accuracyMeters = -1.0)
        }
    }

    @Test
    fun latitudeAt90_isAccepted() {
        val location = Location(latitude = 90.0, longitude = 10.0)
        assertEquals(90.0, location.latitude, 0.0)
    }

    @Test
    fun longitudeAbove180_isRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            Location(latitude = 10.0, longitude = 181.0)
        }
    }
}