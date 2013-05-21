package fr.xgouchet.packageexplorer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StanleySecretCodeReceiver extends BroadcastReceiver {

	/**
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
			Intent secret = new Intent(context, StanleySecretPreferences.class);
			secret.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(secret);
		}
	}

}
