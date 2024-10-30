import com.dpudov.domain.model.Frame
import com.dpudov.domain.repository.IFrameRepository
import java.util.UUID

class FrameRepository(
    private val localDaoService: IFrameDaoService
) : IFrameRepository {
    override suspend fun loadNextFrames(
        animationId: UUID,
        lastFrameId: UUID?,
        pageSize: Int
    ): List<Frame> = localDaoService.loadNextFrames(
        animationId = animationId,
        lastFrameId = lastFrameId,
        pageSize = pageSize
    )

    override suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame> = localDaoService.loadPreviousFrames(
        animationId = animationId,
        firstFrameId = firstFrameId,
        pageSize = pageSize
    )

    override suspend fun addFrame(frame: Frame) {
        localDaoService.addFrame(frame)
    }

    override suspend fun removeFrame(frame: Frame) {
        localDaoService.removeFrame(frame)
    }
}