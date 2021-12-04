package co.abacus.android.dbconnector.demo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.abacus.android.dbconnector.demo.application.AbacusDemoApplication
import co.abacus.android.dbconnector.demo.databinding.ActivitySplashBinding
import co.abacus.android.dbconnector.demo.util.stdDispatchers
import co.abacus.dbconnector.AuthService
import co.abacus.dbconnector.data.Company
import io.reactivex.disposables.Disposable

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    lateinit var authService: AuthService

    private var authSub: Disposable? = null

    private val LOGGED_OUT = Company()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        authService = (application as AbacusDemoApplication).getAuthService()

        setContentView(binding.root)

        authSub = authService.getCompanyDetails()
            .toSingle(LOGGED_OUT)
            .stdDispatchers()
            .subscribe(
                { company ->
                    if (company.localId == LOGGED_OUT.localId) {
                        gotoLogin()
                    } else {
                        gotoMain()
                    }
                },
                {  err ->
                    // TODO: log error
                    Toast.makeText(this, err.message ?: "Error", Toast.LENGTH_SHORT).show()
                    gotoLogin()
                })
    }

    private fun gotoMain() {
        startActivity(Intent(this, DatabaseDetailActivity::class.java))
        finish()
    }

    private fun gotoLogin() {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    override fun onStop() {
        super.onStop()
        authSub?.dispose()
        authSub = null
    }
}