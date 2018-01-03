package fr.xgouchet.packageexplorer.core.mvp

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.Cutelry.knife
import io.reactivex.functions.BiConsumer
import kotlin.properties.Delegates.notNull

/**
 * @author Xavier F. Gouchet
 */
abstract class ListFragment<T, P : ListPresenter<T>>(val isFabVisible: Boolean)
    : Fragment(),
        ListDisplayer<T>,
        BiConsumer<T, View?> {

    internal val list: RecyclerView by knife(android.R.id.list)
    internal val loading: ProgressBar by knife(R.id.loading)
    internal val fab: FloatingActionButton by knife(R.id.fab)
    internal val empty: View by knife(R.id.empty)

    var presenter: P by notNull()

    abstract val adapter: BaseAdapter<T>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun setLoading(isLoading: Boolean) {
        loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        fab.visibility = if (isLoading || !isFabVisible) View.GONE else View.VISIBLE
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

    override fun accept(t: T, v: View?) {
        presenter.itemSelected(t, v)
    }
}