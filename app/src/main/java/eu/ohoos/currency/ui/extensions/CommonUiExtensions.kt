package eu.ohoos.currency.ui.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun View.gone() {
    visibility = View.GONE
}

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.snackBar(
    text: CharSequence,
    length: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionButtonTitle: Int
) {
    Snackbar.make(this, text, length).show()
}

fun View.snackBarWithAction(
    @StringRes message: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    @StringRes actionButtonTitle: Int,
    onButtonClickListener: (() -> Unit)? = null
) {
    Snackbar.make(this, message, length)
        .setAction(actionButtonTitle) {
            onButtonClickListener?.invoke()
        }
        .show()
}

fun TextInputEditText.getQueryTextChangeStateFlow(): Flow<String> =
    MutableStateFlow("").apply {
        doAfterTextChanged {
            value = it?.toString() ?: ""
        }
    }