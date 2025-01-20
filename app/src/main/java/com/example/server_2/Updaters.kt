import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogViewModel : ViewModel() {
    // LiveData variable for holding the text data
    private val _text = MutableLiveData<String>()

    // LiveData property for exposing text data to outside this class
    val text: LiveData<String> get() = _text

    // Method to update the text
    fun updateText(newText: String) {
        _text.value = newText
    }

    fun postText(newText: String) {
        _text.postValue(newText)
    }
}

class HostIpViewModel : ViewModel() {
    // LiveData variable for holding the text data
    private val _host_ip = MutableLiveData<String>()

    // LiveData property for exposing text data to outside this class
    val text: LiveData<String> get() = _host_ip

    // Method to update the text
    fun updateText(newText: String) {
        _host_ip.value = newText
    }

    fun postText(newText: String) {
        _host_ip.postValue(newText)
    }
}
