package fr.xgouchet.packageexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class StanleySecretCodeReceiver extends BroadcastReceiver {

	private static final String INTENT_SECRET_CODE = "android.provider.Telephony.SECRET_CODE";
	private static final String NUMBER_SECRET = "20041984";

	/**
	 * 5484647866
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		// Check intent action
		if (!INTENT_SECRET_CODE.equals(intent.getAction())) {
			return;
		}

		// Check code
		Uri data = intent.getData();
		String code = data.getHost();
		if (!NUMBER_SECRET.equals(code)) {
			return;
		}

		// Actually do it
		Intent secret = new Intent(context, StanleySecretPreferences.class);
		secret.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(secret);

	}
}
