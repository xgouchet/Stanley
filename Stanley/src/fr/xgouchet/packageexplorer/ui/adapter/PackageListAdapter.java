package fr.xgouchet.packageexplorer.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.common.Constants;

public class PackageListAdapter extends ArrayAdapter<PackageInfo> {

	public PackageListAdapter(final Context context,
			final List<PackageInfo> objects) {
		super(context, R.layout.item_app, objects);

		mPackageManager = getContext().getPackageManager();

	}

	/**
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			v = LayoutInflater.from(getContext()).inflate(R.layout.item_app,
					parent, false);
		}

		boolean highlighted = false;
		boolean tryQuery = !TextUtils.isEmpty(mQuery);

		PackageInfo packageInfo = getItem(position);
		ApplicationInfo info = packageInfo.applicationInfo;

		if (info != null) {

			String name = mPackageManager.getApplicationLabel(info).toString();
			if (tryQuery) {
				highlighted = (name.toLowerCase().contains(mQuery));
			}

			((TextView) v.findViewById(R.id.textAppName)).setText(name);

			((ImageView) v.findViewById(R.id.imageAppIcon))
					.setImageDrawable(mPackageManager.getApplicationIcon(info));

			String subtitle = "";
			switch (mSortMethod) {
			case Constants.SORT_BY_PACKAGE:
				subtitle = packageInfo.packageName;
				if (tryQuery) {
					highlighted |= packageInfo.packageName.toLowerCase()
							.contains(mQuery);
				}
				break;
			case Constants.SORT_BY_INSTALL:
				subtitle = DateFormat.getLongDateFormat(getContext())
						.format(packageInfo.firstInstallTime).toString();
				break;
			case Constants.SORT_BY_UPDATE:
				subtitle = DateFormat.getLongDateFormat(getContext())
						.format(packageInfo.lastUpdateTime).toString();
				break;
			}

			((TextView) v.findViewById(R.id.textSubTitle)).setText(subtitle);
		}

		if (highlighted) {
			v.setBackgroundResource(R.drawable.list_focused);
		} else {
			v.setBackgroundResource(R.drawable.selectable_background);
		}

		return v;
	}

	/**
	 * 
	 */
	public void setSortMethod(final int mSortMethod) {
		this.mSortMethod = mSortMethod;
	}

	public void setQuery(final String query) {
		mQuery = query;
		notifyDataSetChanged();
	}

	protected String mQuery;
	protected PackageManager mPackageManager;
	protected int mSortMethod;

}
