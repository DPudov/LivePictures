import com.dpudov.domain.Stroke
import java.util.UUID

class StrokeRepository(
    private val localDaoService: IStrokeDaoService
) : IStrokeDaoService {
    override suspend fun addStroke(stroke: Stroke) {
        localDaoService.addStroke(stroke)
    }

    override suspend fun removeStroke(strokeId: UUID) {
        localDaoService.removeStroke(strokeId)
    }
}