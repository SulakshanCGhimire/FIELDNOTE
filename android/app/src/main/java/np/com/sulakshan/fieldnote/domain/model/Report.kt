package np.com.sulakshan.fieldnote.domain.model

import java.time.Instant

data class Report(
    val id: String,
    val title: String,
    val categoryId: String,
    val description: String? = null,
    val priority: Priority,
    val status: ReportStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val location: Location? = null,
)