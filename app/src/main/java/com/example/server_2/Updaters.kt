import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogViewModel : ViewModel() {
    // LiveData for holding the text data
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    // Method to update the text
    fun updateText(newText: String) {
        _text.value = newText
    }
}
