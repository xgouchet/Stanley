package fr.xgouchet.packageexplorer.ui.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.common.Settings;
import fr.xgouchet.packageexplorer.ui.PackageStyler;

public class ResolveInfoAdapter extends ArrayAdapter<ResolveInfo> {

	public ResolveInfoAdapter(Context context, ResolveInfo[] objects) {
		super(context, R.layout.item_resolve_info, android.R.id.title, objects);
		mLayoutInflater = LayoutInflater.from(getContext());
		mPackageManager = context.getPackageManager();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.item_resolve_info, parent,
					false);
		}

		ResolveInfo info = getItem(position);
		ComponentName component = new ComponentName(
				info.activityInfo.packageName, info.activityInfo.name);

		TextView textView = (TextView) view.findViewById(android.R.id.title);
		Drawable icon = null;
		String title = null;

		try {
			icon = mPackageManager.getActivityIcon(component);
		} catch (NameNotFoundException e) {
			icon = getContext().getResources().getDrawable(
					R.drawable.ic_no_launcher);
		}

		title = info.activityInfo.name;
		if (Settings.sSimplifyNames) {
			title = PackageStyler.simplifyName(title,
					info.activityInfo.packageName);
		}

		textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		textView.setText(title);

		return view;
	}

	private LayoutInflater mLayoutInflater;
	private PackageManager mPackageManager;
}
