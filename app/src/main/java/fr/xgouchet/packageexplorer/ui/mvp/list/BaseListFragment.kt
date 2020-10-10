package fr.xgouchet.packageexplorer.ui.mvp.list

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.Cutelry.knife
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.Presenter
import io.reactivex.functions.BiConsumer

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseListFragment<T, P : ListPresenter<T>> :
        Fragment(),
        ListDisplayer<T>,
        BiConsumer<T, View?> {

    internal val list: RecyclerView by knife(android.R.id.list)
    private val loading: ProgressBar by knife(R.id.loading)
    private val fab: FloatingActionButton by knife(R.id.fab)
    private val empty: View by knife(R.id.empty)

    protected lateinit var presenter: P

    abstract val adapter: BaseAdapter<T>
    abstract val isFabVisible: Boolean
    abstract val fabIconOverride: Int?

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter

        val iconOverride = fabIconOverride
        if (iconOverride != null) {
            fab.setImageResource(iconOverride)
        }
    }

    // endregion

    // region Displayer

    override fun setPresenter(presenter: Presenter<List<T>>) {
        @Suppress("UNCHECKED_CAST")
        this.presenter = presenter as P
    }

    override fun setLoading(isLoading: Boolean) {
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        (fab as View).visibility = if (isLoading || !isFabVisible) View.GONE else View.VISIBLE
    }

    override fun setError(throwable: Throwable) {
        empty.visibility = View.GONE
        throwable.printStackTrace()
        Snackbar.make(list, "Error: ${throwable.message}", Snackbar.LENGTH_LONG).show()
    }

    override fun setEmpty() {
        adapter.update(listOf())
        empty.visibility = View.VISIBLE
    }

    override fun setContent(content: List<T>) {
        adapter.update(content)
        empty.visibility = View.GONE
    }

    // endregion

    // region BiConsumer<T, View?>

    override fun accept(t: T, v: View?) {
        presenter.itemSelected(t)
    }

    // endregion

    // region Permissions

    fun checkPermission(permission: String): Boolean {
        val currentActivity = activity ?: return false
        val status = ContextCompat.checkSelfPermission(currentActivity, permission)
        return status == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String, requestId: Int) {
        val currentActivity = activity ?: return
        if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, permission)) {
            explainAndRequestPermission(permission, requestId)
        } else {
            doRequestStoragePermission(permission, requestId)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(requestCode)
        } else {
            explainAndRequestPermission(permissions.first(), requestCode)
        }
    }

    protected open fun onPermissionGranted(requestCode: Int) {
        presenter.onPermissionGranted(requestCode)
    }

    @StringRes
    protected open fun getPermissionExplanation(permission: String): Int {
        return 0
    }

    private fun doRequestStoragePermission(permission: String, requestId: Int) {
        val res = getPermissionExplanation(permission)
        if (res != 0) {
            Snackbar.make(list, res, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { doRequestStoragePermission(permission, requestId) }
                    .show()
        }
    }

    private fun explainAndRequestPermission(permission: String, requestId: Int) {
        requestPermissions(arrayOf(permission), requestId)
    }
}
