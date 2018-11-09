package fr.xgouchet.packageexplorer.ui.mvp.list

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.Cutelry.knife
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.Presenter
import io.reactivex.functions.BiConsumer

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseListFragment<T, P : ListPresenter<T>>
    : Fragment(),
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
}