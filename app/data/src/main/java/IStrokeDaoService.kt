import com.dpudov.domain.Stroke
import java.util.UUID

interface IStrokeDaoService {
    suspend fun addStroke(stroke: Stroke)

    suspend fun removeStroke(strokeId: UUID)
}