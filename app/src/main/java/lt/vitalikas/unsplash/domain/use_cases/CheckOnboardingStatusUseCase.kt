package lt.vitalikas.unsplash.domain.use_cases

interface CheckOnboardingStatusUseCase {
    suspend operator fun invoke(key: String, value: Boolean): Boolean
}