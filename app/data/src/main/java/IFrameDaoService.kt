import com.dpudov.domain.Frame
import java.util.UUID

interface IFrameDaoService {
    suspend fun loadNextFrames(animationId: UUID, lastFrameId: UUID?, pageSize: Int): List<Frame>

    suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame>

    suspend fun addFrame(frame: Frame)

    suspend fun removeFrame(frameId: UUID)
}