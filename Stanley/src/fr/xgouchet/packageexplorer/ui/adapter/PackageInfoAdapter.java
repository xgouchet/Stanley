package fr.xgouchet.packageexplorer.ui.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.ManifestInfo;
import fr.xgouchet.packageexplorer.ui.PackageStyler;
import fr.xgouchet.packageexplorer.ui.PermissionStyler;

/**
 * 
 */
public class PackageInfoAdapter implements ExpandableListAdapter {

	public static final int INDEX_INFORMATION = 0;
	public static final int INDEX_FEATURES = 1;
	public static final int INDEX_CUSTOM_PERMISSIONS = 2;
	public static final int INDEX_USED_PERMISSIONS = 3;

	public static final int INDEX_ACTIVITIES = 4;
	public static final int INDEX_SERVICES = 5;
	public static final int INDEX_RECEIVERS = 6;
	public static final int INDEX_PROVIDERS = 7;

	public static final int INDEX_MAX = 8;

	protected Context mContext;
	protected PackageInfo mPackageInfo;
	protected ManifestInfo mManifestInfo;

	public PackageInfoAdapter(Context context, PackageInfo info,
			ManifestInfo manifest) {
		mContext = context;
		mPackageInfo = info;
		mManifestInfo = manifest;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#areAllItemsEnabled()
	 */
	public boolean areAllItemsEnabled() {
		return true;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	public Object getChild(int groupPosition, int childPosition) {
		Object obj = null;

		switch (groupPosition) {
		case INDEX_INFORMATION:
			// TODO show information
			break;
		case INDEX_ACTIVITIES:
			if (mPackageInfo.activities != null) {
				obj = mPackageInfo.activities[childPosition];
			}
			break;
		case INDEX_SERVICES:
			if (mPackageInfo.services != null) {
				obj = mPackageInfo.services[childPosition];
			}
			break;
		case INDEX_RECEIVERS:
			if (mPackageInfo.receivers != null) {
				obj = mPackageInfo.receivers[childPosition];
			}
			break;
		case INDEX_PROVIDERS:
			if (mPackageInfo.providers != null) {
				obj = mPackageInfo.providers[childPosition];
			}
			break;
		case INDEX_USED_PERMISSIONS:
			if (mPackageInfo.requestedPermissions != null) {
				obj = mPackageInfo.requestedPermissions[childPosition];
			}
			break;
		case INDEX_CUSTOM_PERMISSIONS:
			if (mPackageInfo.permissions != null) {
				obj = mPackageInfo.permissions[childPosition];
			}
			break;
		case INDEX_FEATURES:
			if (mPackageInfo.reqFeatures != null) {
				obj = mPackageInfo.reqFeatures[childPosition];
			}
			break;
		default:
			break;
		}

		return obj;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	public int getChildrenCount(int groupPosition) {
		int count = 0;

		switch (groupPosition) {
		case INDEX_INFORMATION:
			count = PackageStyler.getAppInfoCount();
			break;
		case INDEX_ACTIVITIES:
			if (mPackageInfo.activities != null) {
				count = mPackageInfo.activities.length;
			}
			break;
		case INDEX_SERVICES:
			if (mPackageInfo.services != null) {
				count = mPackageInfo.services.length;
			}
			break;
		case INDEX_RECEIVERS:
			if (mPackageInfo.receivers != null) {
				count = mPackageInfo.receivers.length;
			}
			break;
		case INDEX_PROVIDERS:
			if (mPackageInfo.providers != null) {
				count = mPackageInfo.providers.length;
			}
			break;
		case INDEX_USED_PERMISSIONS:
			if (mPackageInfo.requestedPermissions != null) {
				count = mPackageInfo.requestedPermissions.length;
			}
			break;
		case INDEX_CUSTOM_PERMISSIONS:
			if (mPackageInfo.permissions != null) {
				count = mPackageInfo.permissions.length;
			}
			break;
		case INDEX_FEATURES:
			if (mPackageInfo.reqFeatures != null) {
				count = mPackageInfo.reqFeatures.length;
			}
			break;
		default:
			break;
		}

		return count;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 */
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = LayoutInflater.from(mContext).inflate(R.layout.item_child,
					parent, false);
		}

		TextView title;

		title = (TextView) v.findViewById(R.id.textTitle);

		String name = "";

		switch (groupPosition) {
		case INDEX_INFORMATION:
			name = PackageStyler.getAppInfo(childPosition, mPackageInfo,
					mManifestInfo);
			break;
		case INDEX_ACTIVITIES:
			name = mPackageInfo.activities[childPosition].name;
			name = PackageStyler.getLocalName(name, mPackageInfo.packageName);
			break;
		case INDEX_SERVICES:
			name = mPackageInfo.services[childPosition].name;
			name = PackageStyler.getLocalName(name, mPackageInfo.packageName);
			break;
		case INDEX_RECEIVERS:
			name = mPackageInfo.receivers[childPosition].name;
			name = PackageStyler.getLocalName(name, mPackageInfo.packageName);
			break;
		case INDEX_PROVIDERS:
			name = mPackageInfo.providers[childPosition].name;
			name = PackageStyler.getLocalName(name, mPackageInfo.packageName);
			break;
		case INDEX_USED_PERMISSIONS:
			name = mPackageInfo.requestedPermissions[childPosition];
			name = PermissionStyler.getPermissionName(name, mContext);
			break;
		case INDEX_CUSTOM_PERMISSIONS:
			name = mPackageInfo.permissions[childPosition].name;
			name = PermissionStyler.getPermissionName(name, mContext);
			break;
		case INDEX_FEATURES:
			if (mPackageInfo.reqFeatures != null) {
				name = PackageStyler
						.getFeature(mPackageInfo.reqFeatures[childPosition]);
			}
			break;
		}

		title.setText(name);
		return v;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getCombinedChildId(long, long)
	 */
	public long getCombinedChildId(long groupId, long childId) {
		return (childId << 32) + (groupId & 0xFFFFFFFF);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getCombinedGroupId(long)
	 */
	public long getCombinedGroupId(long groupId) {
		return groupId;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	public Object getGroup(int groupPosition) {
		Object obj = null;

		switch (groupPosition) {
		case INDEX_INFORMATION:
			obj = "Mixc info";
			break;
		case INDEX_ACTIVITIES:
			obj = mPackageInfo.activities;
			break;
		case INDEX_SERVICES:
			obj = mPackageInfo.services;
			break;
		case INDEX_RECEIVERS:
			obj = mPackageInfo.receivers;
			break;
		case INDEX_PROVIDERS:
			obj = mPackageInfo.providers;
			break;
		case INDEX_USED_PERMISSIONS:
			obj = mPackageInfo.requestedPermissions;
			break;
		case INDEX_CUSTOM_PERMISSIONS:
			obj = mPackageInfo.permissions;
			break;
		case INDEX_FEATURES:
			obj = mPackageInfo.reqFeatures;
			break;
		default:

			break;
		}

		return obj;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	public int getGroupCount() {
		return INDEX_MAX;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 */
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			v = LayoutInflater.from(mContext).inflate(R.layout.item_group,
					parent, false);
		}

		int txtColor;
		txtColor = mContext.getResources().getColor(R.color.text_accent);

		TextView title;
		ImageView icon, decorator;

		title = (TextView) v.findViewById(R.id.textTitle);
		icon = (ImageView) v.findViewById(R.id.imageIcon);
		decorator = (ImageView) v.findViewById(R.id.imageDecorator);

		if (getChildrenCount(groupPosition) == 0) {
			v.setEnabled(false);
			txtColor = mContext.getResources().getColor(R.color.text_disabled);
			decorator.setVisibility(View.GONE);
		} else {
			v.setEnabled(true);
			decorator.setVisibility(View.VISIBLE);

			if (isExpanded) {
				decorator.setImageResource(R.drawable.ic_action_collapse);
			} else {
				decorator.setImageResource(R.drawable.ic_action_expand);
			}
		}

		title.setTextColor(txtColor);

		switch (groupPosition) {
		case INDEX_INFORMATION:
			title.setText("App Information");
			icon.setImageResource(R.drawable.ic_info);
			break;
		case INDEX_ACTIVITIES:
			title.setText("Activities");
			icon.setImageResource(R.drawable.ic_activities);
			break;
		case INDEX_SERVICES:
			title.setText("Services");
			icon.setImageResource(R.drawable.ic_services);
			break;
		case INDEX_RECEIVERS:
			title.setText("Broadcast receivers");
			icon.setImageResource(R.drawable.ic_receivers);
			break;
		case INDEX_PROVIDERS:
			title.setText("Content providers");
			icon.setImageResource(R.drawable.ic_providers);
			break;
		case INDEX_USED_PERMISSIONS:
			title.setText("Permissions");
			icon.setImageResource(R.drawable.ic_permissions);
			break;
		case INDEX_CUSTOM_PERMISSIONS:
			title.setText("Pacakge custom permissions");
			icon.setImageResource(R.drawable.ic_custom_permissions);
			break;
		case INDEX_FEATURES:
			title.setText("Features required");
			icon.setImageResource(R.drawable.ic_features);
			break;
		}

		return v;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isEmpty()
	 */
	public boolean isEmpty() {
		return false;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#onGroupCollapsed(int)
	 */
	public void onGroupCollapsed(int groupPosition) {

	}

	/**
	 * @see android.widget.ExpandableListAdapter#onGroupExpanded(int)
	 */
	public void onGroupExpanded(int groupPosition) {

	}

	/**
	 * @see android.widget.ExpandableListAdapter#registerDataSetObserver(android.database.DataSetObserver)
	 */
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	/**
	 * @see android.widget.ExpandableListAdapter#unregisterDataSetObserver(android.database.DataSetObserver)
	 */
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

}
