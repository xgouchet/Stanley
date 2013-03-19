package fr.xgouchet.packageexplorer.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.Settings;

public class PackageListAdapter extends ArrayAdapter<PackageInfo> {

	public PackageListAdapter(Context context, List<PackageInfo> objects) {
		super(context, R.layout.item_app, objects);

		mPackageManager = getContext().getPackageManager();
	}

	/**
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			v = LayoutInflater.from(getContext()).inflate(R.layout.item_app,
					parent, false);
		}

		PackageInfo packageInfo = getItem(position);
		ApplicationInfo info = packageInfo.applicationInfo;

		if (info != null) {

			((TextView) v.findViewById(R.id.textAppName))
					.setText(mPackageManager.getApplicationLabel(info));

			((ImageView) v.findViewById(R.id.imageAppIcon))
					.setImageDrawable(mPackageManager.getApplicationIcon(info));

			String subtitle = "";
			switch (Settings.sSortMethod) {
			case Constants.SORT_BY_PACKAGE:
				subtitle = packageInfo.packageName;
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

		return v;
	}

	protected PackageManager mPackageManager;
}
