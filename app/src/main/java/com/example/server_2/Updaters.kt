import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogViewModel : ViewModel() {
    // LiveData variable for holding the text data
    var _text: MutableLiveData<String> = MutableLiveData<String>()

    //  LiveData property for exposing text data to outside this class
    //val text: LiveData<String> get() = _text

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
    var _ip: MutableLiveData<String> = MutableLiveData<String>()

    // LiveData property for exposing text data to outside this class
    //val ip: LiveData<String> get() = _ip

    // Method to update the text
    fun updateText(newText: String) {
        _ip.value = newText
    }

    fun postText(newText: String) {
        _ip.postValue(newText)
    }
}
