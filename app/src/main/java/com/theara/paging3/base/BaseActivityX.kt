import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivityX<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    AppCompatActivity() {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    open fun T.initialize() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)

        // Optionally set lifecycle owner if needed
        binding.lifecycleOwner = this

        // Calling the extension function
        binding.initialize()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showProgress(view: View?) {
        view?.visibility = View.VISIBLE
    }

    fun hideProgress(view: View?) {
        view?.visibility = View.GONE
    }
}