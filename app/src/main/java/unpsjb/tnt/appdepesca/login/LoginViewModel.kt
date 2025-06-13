package unpsjb.tnt.appdepesca.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

open class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    open val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    open val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    open val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    open val isLoading: LiveData<Boolean> = _isLoading

    private val _isInvalid = MutableLiveData<Boolean>()
    open val isInvalid: LiveData<Boolean> = _isInvalid

    open fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 6

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    suspend fun onLoginSelected() {
        _isLoading.value = true
        delay(1000)
        _isLoading.value = false
    }

    open fun credencialesNoValidas() {
        _isInvalid.value = true
    }
}