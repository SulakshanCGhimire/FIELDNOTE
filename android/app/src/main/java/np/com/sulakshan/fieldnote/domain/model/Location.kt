package np.com.sulakshan.fieldnote.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double? = null,
) {
    init {
        require(latitude in -90.0..90.0) { "latitude out of range: $latitude" }
        require(longitude in -180.0..180.0) { "longitude out of range: $longitude" }
        require(accuracyMeters == null || accuracyMeters >= 0) { "accuracy must not be negative" }
    }
}