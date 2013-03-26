package fr.xgouchet.packageexplorer.ui.adapter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;

public class ResourcesAdapter implements ExpandableListAdapter {

	/**
	 * 
	 * @param context
	 * @param folder
	 */
	public ResourcesAdapter(final Activity activity, final File folder) {
		mContext = activity;
		mInflater = LayoutInflater.from(activity);

		mResFolders = new LinkedList<File>();
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				mResFolders.add(file);
			}
		}

		mResources = new SparseArray<List<File>>(mResFolders.size());

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mDensity = metrics.densityDpi;

		mUseDarkBackground = false;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return getGroupCount() == 0;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return mResFolders.size();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public File getGroup(final int groupPosition) {
		return mResFolders.get(groupPosition);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getCombinedGroupId(long)
	 */
	@Override
	public long getCombinedGroupId(final long groupId) {
		return groupId;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			final View convertView, final ViewGroup parent) {
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
		title.setText(getGroup(groupPosition).getName());

		icon.setImageResource(R.drawable.ic_action_folder);

		return v;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(final int groupPosition) {
		return getGroupChildren(groupPosition).size();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public File getChild(final int groupPosition, final int childPosition) {
		return getGroupChildren(groupPosition).get(childPosition);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return childPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getCombinedChildId(long, long)
	 */
	@Override
	public long getCombinedChildId(final long groupId, final long childId) {
		return (childId << 32) + (groupId & 0xFFFFFFFF);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(final int groupPosition,
			final int childPosition) {
		return false;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 *      android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, final View convertView,
			final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.item_res, parent, false);
		}

		File file = getChild(groupPosition, childPosition);

		TextView title = (TextView) view.findViewById(android.R.id.title);
		ImageView image = (ImageView) view.findViewById(android.R.id.icon);

		title.setText(file.getName());

		Drawable drawable = Drawable.createFromPath(file.getPath());

		int imageDensity = getDensityForImage(groupPosition);
		int width, height;
		width = drawable.getIntrinsicWidth();
		height = drawable.getIntrinsicHeight();

		LayoutParams params = new LayoutParams((width * mDensity * 2)
				/ imageDensity, (height * mDensity * 2) / imageDensity);
		params.gravity = Gravity.CENTER;
		image.setLayoutParams(params);

		image.setImageDrawable(drawable);
		image.setScaleType(ScaleType.FIT_XY);

		// styling
		if (mUseDarkBackground) {
			title.setTextColor(Color.WHITE);
			view.setBackgroundColor(Color.DKGRAY);
		} else {
			title.setTextColor(Color.BLACK);
			view.setBackgroundColor(Color.TRANSPARENT);
		}
		return view;
	}

	/**
	 * Notifies the attached observers that the underlying data has been changed
	 * and any View reflecting the data set should refresh itself.
	 */
	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#registerDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void registerDataSetObserver(final DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#unregisterDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void unregisterDataSetObserver(final DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#onGroupCollapsed(int)
	 */
	@Override
	public void onGroupCollapsed(final int groupPosition) {

	}

	/**
	 * @see android.widget.ExpandableListAdapter#onGroupExpanded(int)
	 */
	@Override
	public void onGroupExpanded(final int groupPosition) {

	}

	/**
	 * @param groupPosition
	 * @return
	 */
	private List<File> getGroupChildren(final int groupPosition) {
		List<File> resList;

		resList = mResources.get(groupPosition);

		if (resList == null) {

			resList = new LinkedList<File>();
			File[] files = getGroup(groupPosition).listFiles();

			for (File file : files) {
				if (!file.isDirectory()) {
					resList.add(file);
				}
			}

			mResources.setValueAt(groupPosition, resList);
		}

		return resList;

	}

	/**
	 * 
	 * @param groupPosition
	 * @return
	 */
	@SuppressLint("InlinedApi")
	private int getDensityForImage(final int groupPosition) {
		String name = getGroup(groupPosition).getName();
		int density;

		if (name.contains("xxhdpi")) {
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				density = DisplayMetrics.DENSITY_XXHIGH;
			} else {
				density = DisplayMetrics.DENSITY_XHIGH;
			}
		} else if (name.contains("xhdpi")) {
			density = DisplayMetrics.DENSITY_XHIGH;
		} else if (name.contains("hdpi")) {
			density = DisplayMetrics.DENSITY_HIGH;
		} else if (name.contains("mdpi")) {
			density = DisplayMetrics.DENSITY_MEDIUM;
		} else if (name.contains("ldpi")) {
			density = DisplayMetrics.DENSITY_LOW;
		} else if (name.contains("tvdpi")) {
			if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2) {
				density = DisplayMetrics.DENSITY_TV;
			} else {
				density = DisplayMetrics.DENSITY_HIGH;
			}
		} else {
			density = DisplayMetrics.DENSITY_DEFAULT;
		}

		return density;
	}

	public void switchBackground() {
		mUseDarkBackground = !mUseDarkBackground;
	}

	private Context mContext;
	private LayoutInflater mInflater;
	private List<File> mResFolders;
	private SparseArray<List<File>> mResources;
	private int mDensity;

	private boolean mUseDarkBackground;

	private final DataSetObservable mDataSetObservable = new DataSetObservable();
}
