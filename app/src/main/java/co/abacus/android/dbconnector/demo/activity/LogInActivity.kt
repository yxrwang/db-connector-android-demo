package co.abacus.android.dbconnector.demo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import co.abacus.android.dbconnector.demo.R
import co.abacus.android.dbconnector.demo.application.AbacusDemoApplication
import co.abacus.android.dbconnector.demo.util.stdDispatchers
import co.abacus.dbconnector.AuthService
import co.abacus.dbconnector.domain.input.CompanyAccountSignUpInput
import io.reactivex.disposables.Disposable

class LogInActivity : AppCompatActivity() {

    lateinit var authService: AuthService

    private var authSub: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authService = (application as AbacusDemoApplication).getAuthService()

        setContentView(R.layout.activity_log_in)
        title = "Log In"

        findViewById<Button>(R.id.logIn).setOnClickListener {
            onLogInClicked(
                    findViewById<EditText>(R.id.username).text.toString(),
                    findViewById<EditText>(R.id.password).text.toString()
            )
        }

        findViewById<Button>(R.id.signUp)
            .setOnClickListener {
                onSignUpClicked()
            }
    }

    private fun onLogInClicked(userName: String, password: String) {
        authSub = authService.login(userName, password)
            .stdDispatchers()
            .doFinally { authSub = null }
            .subscribe(
                {
                    startActivity(Intent(this@LogInActivity, DatabaseDetailActivity::class.java))
                    finish()
                },
                { err ->
                    Toast.makeText(this@LogInActivity, err.javaClass.simpleName, Toast.LENGTH_LONG).show()
                }
            )
    }

    private fun onSignUpClicked() {
        authSub = authService.signUp(
            CompanyAccountSignUpInput(
                email = findViewById<EditText>(R.id.username).text.toString(),
                password = findViewById<EditText>(R.id.password).text.toString(),
                companyEmail = findViewById<EditText>(R.id.username).text.toString(),
                companyName = "Test Demo Store",
                companyRegistration = "123456789",
                currency = "AUD",
                customerFName = "Test",
                customerLName = "Account",
                timeZone = "Australia/Sydney"
            )
        )
            .stdDispatchers()
            .doFinally { authSub = null }
            .subscribe(
                {
                    Toast.makeText(this@LogInActivity, "Sign Up success", Toast.LENGTH_LONG).show()
                },
                { err ->
                    Toast.makeText(this@LogInActivity, err.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            )
    }

    override fun onStop() {
        authSub?.dispose()
        authSub = null
        super.onStop()
    }

}