import data.PlayerRepositoryImpl
import domain.api.PlayerInteractor
import domain.api.PlayerRepository
import domain.impl.PlayerInteractorImpl

object Creator {

    fun providePlayerRepository(): PlayerRepository = PlayerRepositoryImpl()

    fun providePlayerInteractor(): PlayerInteractor =
        PlayerInteractorImpl(repository = providePlayerRepository())

}
