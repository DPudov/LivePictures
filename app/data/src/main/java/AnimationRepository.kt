import com.dpudov.domain.Animation
import com.dpudov.domain.IAnimationRepository
import kotlinx.coroutines.flow.Flow

class AnimationRepository(
    private val localDaoService: IAnimationDaoService
): IAnimationRepository {
    override fun getLatestAnimation(): Flow<Animation?> = localDaoService.getLatestAnimation()

    override suspend fun addAnimation(animation: Animation) {
        localDaoService.addAnimation(animation)
    }
}