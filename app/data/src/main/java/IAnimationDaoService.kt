import com.dpudov.domain.Animation
import kotlinx.coroutines.flow.Flow

interface IAnimationDaoService {
    fun getLatestAnimation(): Flow<Animation?>

    suspend fun addAnimation(animation: Animation)
}