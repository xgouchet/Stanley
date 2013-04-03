package fr.xgouchet.packageexplorer.common;

import fr.xgouchet.androidlib.common.AbstractChangeLog;
import fr.xgouchet.packageexplorer.R;

public class StanleyChangeLog extends AbstractChangeLog {

	@Override
	public int getChangeLogResourceForVersion(final int version) {
		return R.string.release1_log;
	}

	@Override
	public int getTitleResourceForVersion(final int version) {
		return R.string.release1;
	}

}
