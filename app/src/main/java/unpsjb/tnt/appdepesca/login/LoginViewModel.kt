package unpsjb.tnt.appdepesca.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

open class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //LiveData
    private val _email = MutableLiveData<String>("")
    open val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>("")
    open val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>(false)
    open val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>(false)
    open val isLoading: LiveData<Boolean> = _isLoading

    private val _isInvalid = MutableLiveData<Boolean>(false)
    open val isInvalid: LiveData<Boolean> = _isInvalid

    //Validaciones Locales
    open fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    //Login real con Firebase
    suspend fun onLoginSelected(): Boolean {
        _isLoading.value = true

        val emailValue = _email.value ?: ""
        val passwordValue = _password.value ?: ""

        return try {
            val result = auth.signInWithEmailAndPassword(emailValue, passwordValue).await()
            _isLoading.value = false
            _isInvalid.value = false
            true // Login OK
        }catch (e: Exception){
            _isLoading.value = false
            _isInvalid.value = true
            false // Login Fail
        }
    }

    fun resetInvalid(){
        _isInvalid.value = false
    }
}