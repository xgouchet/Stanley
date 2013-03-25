package fr.xgouchet.packageexplorer.ui.fragments;

import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.ui.adapter.ResolveInfoAdapter;

public class ResolveInfoFragment extends DialogFragment {

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_resolve_info, container,
				false);

		getDialog().setTitle(R.string.action_open);

		Bundle args = getArguments();
		mInfoList = (ResolveInfo[]) args
				.getParcelableArray(Constants.EXTRA_RESOLVE_INFO);

		mGridView = (AdapterView<ListAdapter>) view
				.findViewById(android.R.id.list);
		mGridView.setAdapter(new ResolveInfoAdapter(getActivity(), mInfoList));
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				dismiss();
				startActivity(PackageUtils
						.getResolvedIntent(mInfoList[position]));

			}
		});

		return view;
	}

	ResolveInfo[] mInfoList;
	AdapterView<ListAdapter> mGridView;

}
