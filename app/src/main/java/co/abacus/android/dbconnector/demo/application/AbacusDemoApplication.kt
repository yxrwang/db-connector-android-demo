package co.abacus.android.dbconnector.demo.application

import android.app.Application
import co.abacus.dbconnector.*

class AbacusDemoApplication : Application() {

    private lateinit var factory: DbConnectorFactory

    override fun onCreate() {
        super.onCreate()
        factory = DbConnectorFactory(this)
    }

    fun getAuthService(): AuthService { return factory.authService }
    fun getCategoriesRepo(): CategoriesRepository { return factory.categoriesRepo }
    fun getProductsRepo(): ProductsRepository { return factory.productsRepo }
    fun getModifiersRepo(): ModifiersRepository { return factory.modifiersRepo }
    fun getImageRepo(): ImageRepository { return factory.imageRepository }
}