package fr.xgouchet.packageexplorer.ui;

import java.util.Locale;

import android.Manifest;
import android.content.Context;
import fr.xgouchet.packageexplorer.R;

@SuppressWarnings("deprecation")
public class PermissionStyler {

	public static final String BILLING = "com.android.vending.BILLING";
	public static final String CHECK_LICENCE = "com.android.vending.CHECK_LICENSE";
	public static final String INSTALL_SHORTCUT = "com.android.launcher.permission.INSTALL_SHORTCUT";
	public static final String UNINSTALL_SHORTCUT = "com.android.launcher.permission.UNINSTALL_SHORTCUT";
	public static final String C2DM_RECEIVE = "com.google.android.c2dm.permission.RECEIVE";

	public static String getPermissionName(final String permission,
			final Context context) {
		String name = permission;

		if (name.equals(Manifest.permission.ACCESS_CHECKIN_PROPERTIES)) {
			name = context.getString(R.string.permlab_checkinProperties);
		} else if (name.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			name = context.getString(R.string.permlab_accessCoarseLocation);
		} else if (name.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
			name = context.getString(R.string.permlab_accessFineLocation);
		} else if (name
				.equals(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)) {
			name = context
					.getString(R.string.permlab_accessLocationExtraCommands);
		} else if (name.equals(Manifest.permission.ACCESS_MOCK_LOCATION)) {
			name = context.getString(R.string.permlab_accessMockLocation);
		} else if (name.equals(Manifest.permission.ACCESS_NETWORK_STATE)) {
			name = context.getString(R.string.permlab_accessNetworkState);
		} else if (name.equals(Manifest.permission.ACCESS_SURFACE_FLINGER)) {
			name = context.getString(R.string.permlab_accessSurfaceFlinger);
		} else if (name.equals(Manifest.permission.ACCESS_WIFI_STATE)) {
			name = context.getString(R.string.permlab_accessWifiState);
		} else if (name.equals(Manifest.permission.ACCOUNT_MANAGER)) {
			name = context.getString(R.string.permlab_accountManagerService);
		} else if (name.equals(Manifest.permission.ADD_VOICEMAIL)) {
			name = context.getString(R.string.permlab_addVoicemail);
		} else if (name.equals(Manifest.permission.AUTHENTICATE_ACCOUNTS)) {
			name = context.getString(R.string.permlab_authenticateAccounts);
		} else if (name.equals(Manifest.permission.BATTERY_STATS)) {
			name = context.getString(R.string.permlab_batteryStats);
		} else if (name.equals(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)) {
			name = context.getString(R.string.permlab_bindAccessibilityService);
		} else if (name.equals(Manifest.permission.BIND_APPWIDGET)) {
			name = context.getString(R.string.permlab_bindRemoteViews);
		} else if (name.equals(Manifest.permission.BIND_DEVICE_ADMIN)) {
			name = context.getString(R.string.permlab_bindDeviceAdmin);
		} else if (name.equals(Manifest.permission.BIND_INPUT_METHOD)) {
			name = context.getString(R.string.permlab_bindInputMethod);
		} else if (name.equals(Manifest.permission.BIND_REMOTEVIEWS)) {
			name = context.getString(R.string.permlab_bindRemoteViews);
		} else if (name.equals(Manifest.permission.BIND_TEXT_SERVICE)) {
			name = context.getString(R.string.permlab_bindTextService);
		} else if (name.equals(Manifest.permission.BIND_VPN_SERVICE)) {
			name = context.getString(R.string.permlab_bindVpnService);
		} else if (name.equals(Manifest.permission.BIND_WALLPAPER)) {
			name = context.getString(R.string.permlab_bindWallpaper);
		} else if (name.equals(Manifest.permission.BLUETOOTH)) {
			name = context.getString(R.string.permlab_bluetooth);
		} else if (name.equals(Manifest.permission.BLUETOOTH_ADMIN)) {
			name = context.getString(R.string.permlab_bluetoothAdmin);
		} else if (name.equals(Manifest.permission.BRICK)) {
			name = context.getString(R.string.permlab_brick);
		} else if (name.equals(Manifest.permission.BROADCAST_PACKAGE_REMOVED)) {
			name = context.getString(R.string.permlab_broadcastPackageRemoved);
		} else if (name.equals(Manifest.permission.BROADCAST_SMS)) {
			name = context.getString(R.string.permlab_broadcastSmsReceived);
		} else if (name.equals(Manifest.permission.BROADCAST_STICKY)) {
			name = context.getString(R.string.permlab_broadcastSticky);
		} else if (name.equals(Manifest.permission.BROADCAST_WAP_PUSH)) {
			name = context.getString(R.string.permlab_broadcastWapPush);
		} else if (name.equals(Manifest.permission.CALL_PHONE)) {
			name = context.getString(R.string.permlab_callPhone);
		} else if (name.equals(Manifest.permission.CALL_PRIVILEGED)) {
			name = context.getString(R.string.permlab_callPrivileged);
		} else if (name.equals(Manifest.permission.CAMERA)) {
			name = context.getString(R.string.permlab_camera);
		} else if (name
				.equals(Manifest.permission.CHANGE_COMPONENT_ENABLED_STATE)) {
			name = context.getString(R.string.permlab_changeComponentState);
		} else if (name.equals(Manifest.permission.CHANGE_CONFIGURATION)) {
			name = context.getString(R.string.permlab_changeConfiguration);
		} else if (name.equals(Manifest.permission.CHANGE_NETWORK_STATE)) {
			name = context.getString(R.string.permlab_changeNetworkState);
		} else if (name.equals(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)) {
			name = context.getString(R.string.permlab_changeWifiMulticastState);
		} else if (name.equals(Manifest.permission.CHANGE_WIFI_STATE)) {
			name = context.getString(R.string.permlab_changeWifiState);
		} else if (name.equals(Manifest.permission.CLEAR_APP_CACHE)) {
			name = context.getString(R.string.permlab_clearAppCache);
		} else if (name.equals(Manifest.permission.CLEAR_APP_USER_DATA)) {
			name = context.getString(R.string.permlab_clearAppUserData);
		} else if (name.equals(Manifest.permission.CONTROL_LOCATION_UPDATES)) {
			name = context.getString(R.string.permlab_locationUpdates);
		} else if (name.equals(Manifest.permission.DELETE_CACHE_FILES)) {
			name = context.getString(R.string.permlab_deleteCacheFiles);
		} else if (name.equals(Manifest.permission.DELETE_PACKAGES)) {
			name = context.getString(R.string.permlab_deletePackages);
		} else if (name.equals(Manifest.permission.DEVICE_POWER)) {
			name = context.getString(R.string.permlab_devicePower);
		} else if (name.equals(Manifest.permission.DIAGNOSTIC)) {
			name = context.getString(R.string.permlab_diagnostic);
		} else if (name.equals(Manifest.permission.DISABLE_KEYGUARD)) {
			name = context.getString(R.string.permlab_disableKeyguard);
		} else if (name.equals(Manifest.permission.DUMP)) {
			name = context.getString(R.string.permlab_dump);
		} else if (name.equals(Manifest.permission.EXPAND_STATUS_BAR)) {
			name = context.getString(R.string.permlab_expandStatusBar);
		} else if (name.equals(Manifest.permission.FACTORY_TEST)) {
			name = context.getString(R.string.permlab_factoryTest);
		} else if (name.equals(Manifest.permission.FLASHLIGHT)) {
			name = context.getString(R.string.permlab_flashlight);
		} else if (name.equals(Manifest.permission.FORCE_BACK)) {
			name = context.getString(R.string.permlab_forceBack);
		} else if (name.equals(Manifest.permission.GET_ACCOUNTS)) {
			name = context.getString(R.string.permlab_getAccounts);
		} else if (name.equals(Manifest.permission.GET_PACKAGE_SIZE)) {
			name = context.getString(R.string.permlab_getPackageSize);
		} else if (name.equals(Manifest.permission.GET_TASKS)) {
			name = context.getString(R.string.permlab_getTasks);
		} else if (name.equals(Manifest.permission.HARDWARE_TEST)) {
			name = context.getString(R.string.permlab_hardware_test);
		} else if (name.equals(Manifest.permission.INJECT_EVENTS)) {
			name = context.getString(R.string.permlab_injectEvents);
		} else if (name.equals(Manifest.permission.INSTALL_LOCATION_PROVIDER)) {
			name = context.getString(R.string.permlab_installLocationProvider);
		} else if (name.equals(Manifest.permission.INSTALL_PACKAGES)) {
			name = context.getString(R.string.permlab_installPackages);
		} else if (name.equals(Manifest.permission.INTERNAL_SYSTEM_WINDOW)) {
			name = context.getString(R.string.permlab_internalSystemWindow);
		} else if (name.equals(Manifest.permission.INTERNET)) {
			name = context.getString(R.string.permlab_createNetworkSockets);
		} else if (name.equals(Manifest.permission.KILL_BACKGROUND_PROCESSES)) {
			name = context.getString(R.string.permlab_killBackgroundProcesses);
		} else if (name.equals(Manifest.permission.MANAGE_ACCOUNTS)) {
			name = context.getString(R.string.permlab_manageAccounts);
		} else if (name.equals(Manifest.permission.MANAGE_APP_TOKENS)) {
			name = context.getString(R.string.permlab_manageAppTokens);
		} else if (name.equals(Manifest.permission.MASTER_CLEAR)) {
			name = context.getString(R.string.permlab_masterClear);
		} else if (name.equals(Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
			name = context.getString(R.string.permlab_modifyAudioSettings);
		} else if (name.equals(Manifest.permission.MODIFY_PHONE_STATE)) {
			name = context.getString(R.string.permlab_modifyPhoneState);
		} else if (name.equals(Manifest.permission.MOUNT_FORMAT_FILESYSTEMS)) {
			name = context.getString(R.string.permlab_mount_format_filesystems);
		} else if (name.equals(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)) {
			name = context
					.getString(R.string.permlab_mount_unmount_filesystems);
		} else if (name.equals(Manifest.permission.NFC)) {
			name = context.getString(R.string.permlab_nfc);
		} else if (name.equals(Manifest.permission.PERSISTENT_ACTIVITY)) {
			name = context.getString(R.string.permlab_persistentActivity);
		} else if (name.equals(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
			name = context.getString(R.string.permlab_processOutgoingCalls);
		} else if (name.equals(Manifest.permission.READ_CALENDAR)) {
			name = context.getString(R.string.permlab_readCalendar);
		} else if (name.equals(Manifest.permission.READ_CALL_LOG)) {
			name = context.getString(R.string.permlab_readCallLog);
		} else if (name.equals(Manifest.permission.READ_CONTACTS)) {
			name = context.getString(R.string.permlab_readContacts);
		} else if (name.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
			name = context.getString(R.string.permlab_sdcardRead);
		} else if (name.equals(Manifest.permission.READ_FRAME_BUFFER)) {
			name = context.getString(R.string.permlab_readFrameBuffer);
		} else if (name.equals(Manifest.permission.READ_INPUT_STATE)) {
			name = context.getString(R.string.permlab_readInputState);
		} else if (name.equals(Manifest.permission.READ_HISTORY_BOOKMARKS)) {
			name = context.getString(R.string.permlab_readHistoryBookmarks);
		} else if (name.equals(Manifest.permission.READ_LOGS)) {
			name = context.getString(R.string.permlab_readLogs);
		} else if (name.equals(Manifest.permission.READ_PHONE_STATE)) {
			name = context.getString(R.string.permlab_readPhoneState);
		} else if (name.equals(Manifest.permission.READ_PROFILE)) {
			name = context.getString(R.string.permlab_readProfile);
		} else if (name.equals(Manifest.permission.READ_SMS)) {
			name = context.getString(R.string.permlab_readSms);
		} else if (name.equals(Manifest.permission.READ_SOCIAL_STREAM)) {
			name = context.getString(R.string.permlab_readSocialStream);
		} else if (name.equals(Manifest.permission.READ_SYNC_SETTINGS)) {
			name = context.getString(R.string.permlab_readSyncSettings);
		} else if (name.equals(Manifest.permission.READ_SYNC_STATS)) {
			name = context.getString(R.string.permlab_readSyncStats);
		} else if (name.equals(Manifest.permission.READ_USER_DICTIONARY)) {
			name = context.getString(R.string.permlab_readDictionary);
		} else if (name.equals(Manifest.permission.REBOOT)) {
			name = context.getString(R.string.permlab_reboot);
		} else if (name.equals(Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
			name = context.getString(R.string.permlab_receiveBootCompleted);
		} else if (name.equals(Manifest.permission.RECEIVE_MMS)) {
			name = context.getString(R.string.permlab_receiveMms);
		} else if (name.equals(Manifest.permission.RECEIVE_SMS)) {
			name = context.getString(R.string.permlab_receiveSms);
		} else if (name.equals(Manifest.permission.RECEIVE_WAP_PUSH)) {
			name = context.getString(R.string.permlab_receiveWapPush);
		} else if (name.equals(Manifest.permission.RECORD_AUDIO)) {
			name = context.getString(R.string.permlab_recordAudio);
		} else if (name.equals(Manifest.permission.REORDER_TASKS)) {
			name = context.getString(R.string.permlab_reorderTasks);
		} else if (name.equals(Manifest.permission.SEND_SMS)) {
			name = context.getString(R.string.permlab_sendSms);
		} else if (name.equals(Manifest.permission.SET_ACTIVITY_WATCHER)) {
			name = context.getString(R.string.permlab_runSetActivityWatcher);
		} else if (name.equals(Manifest.permission.SET_ALARM)) {
			name = context.getString(R.string.permlab_setAlarm);
		} else if (name.equals(Manifest.permission.SET_ALWAYS_FINISH)) {
			name = context.getString(R.string.permlab_setAlwaysFinish);
		} else if (name.equals(Manifest.permission.SET_ANIMATION_SCALE)) {
			name = context.getString(R.string.permlab_setAnimationScale);
		} else if (name.equals(Manifest.permission.SET_DEBUG_APP)) {
			name = context.getString(R.string.permlab_setDebugApp);
		} else if (name.equals(Manifest.permission.SET_ORIENTATION)) {
			name = context.getString(R.string.permlab_setOrientation);
		} else if (name.equals(Manifest.permission.SET_POINTER_SPEED)) {
			name = context.getString(R.string.permlab_setPointerSpeed);
		} else if (name.equals(Manifest.permission.SET_PREFERRED_APPLICATIONS)) {
			name = context.getString(R.string.permlab_setPreferredApplications);
		} else if (name.equals(Manifest.permission.SET_PROCESS_LIMIT)) {
			name = context.getString(R.string.permlab_setProcessLimit);
		} else if (name.equals(Manifest.permission.SET_TIME)) {
			name = context.getString(R.string.permlab_setTime);
		} else if (name.equals(Manifest.permission.SET_TIME_ZONE)) {
			name = context.getString(R.string.permlab_setTimeZone);
		} else if (name.equals(Manifest.permission.SET_WALLPAPER)) {
			name = context.getString(R.string.permlab_setWallpaper);
		} else if (name.equals(Manifest.permission.SET_WALLPAPER_HINTS)) {
			name = context.getString(R.string.permlab_setWallpaperHints);
		} else if (name.equals(Manifest.permission.SIGNAL_PERSISTENT_PROCESSES)) {
			name = context
					.getString(R.string.permlab_signalPersistentProcesses);
		} else if (name.equals(Manifest.permission.STATUS_BAR)) {
			name = context.getString(R.string.permlab_statusBar);
		} else if (name.equals(Manifest.permission.SUBSCRIBED_FEEDS_READ)) {
			name = context.getString(R.string.permlab_subscribedFeedsRead);
		} else if (name.equals(Manifest.permission.SUBSCRIBED_FEEDS_WRITE)) {
			name = context.getString(R.string.permlab_subscribedFeedsWrite);
		} else if (name.equals(Manifest.permission.USE_CREDENTIALS)) {
			name = context.getString(R.string.permlab_useCredentials);
		} else if (name.equals(Manifest.permission.USE_SIP)) {
			name = context.getString(R.string.permlab_use_sip);
		} else if (name.equals(Manifest.permission.VIBRATE)) {
			name = context.getString(R.string.permlab_vibrate);
		} else if (name.equals(Manifest.permission.WAKE_LOCK)) {
			name = context.getString(R.string.permlab_wakeLock);
		} else if (name.equals(Manifest.permission.WRITE_APN_SETTINGS)) {
			name = context.getString(R.string.permlab_writeApnSettings);
		} else if (name.equals(Manifest.permission.WRITE_CALENDAR)) {
			name = context.getString(R.string.permlab_writeCalendar);
		} else if (name.equals(Manifest.permission.WRITE_CALL_LOG)) {
			name = context.getString(R.string.permlab_writeCallLog);
		} else if (name.equals(Manifest.permission.WRITE_CONTACTS)) {
			name = context.getString(R.string.permlab_writeContacts);
		} else if (name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			name = context.getString(R.string.permlab_sdcardWrite);
		} else if (name.equals(Manifest.permission.WRITE_GSERVICES)) {
			name = context.getString(R.string.permlab_writeGservices);
		} else if (name.equals(Manifest.permission.WRITE_HISTORY_BOOKMARKS)) {
			name = context.getString(R.string.permlab_writeHistoryBookmarks);
		} else if (name.equals(Manifest.permission.WRITE_PROFILE)) {
			name = context.getString(R.string.permlab_writeProfile);
		} else if (name.equals(Manifest.permission.WRITE_SECURE_SETTINGS)) {
			name = context.getString(R.string.permlab_writeSecureSettings);
		} else if (name.equals(Manifest.permission.WRITE_SETTINGS)) {
			name = context.getString(R.string.permlab_writeSettings);
		} else if (name.equals(Manifest.permission.WRITE_SMS)) {
			name = context.getString(R.string.permlab_writeSms);
		} else if (name.equals(Manifest.permission.WRITE_SOCIAL_STREAM)) {
			name = context.getString(R.string.permlab_writeSocialStream);
		} else if (name.equals(Manifest.permission.WRITE_SYNC_SETTINGS)) {
			name = context.getString(R.string.permlab_writeSyncSettings);
		} else if (name.equals(Manifest.permission.WRITE_USER_DICTIONARY)) {
			name = context.getString(R.string.permlab_writeDictionary);
		} else if (name.equals(BILLING)) {
			name = context.getString(R.string.permlab_billing);
		} else if (name.equals(CHECK_LICENCE)) {
			name = context.getString(R.string.permlab_checkLicence);
		} else if (name.equals(INSTALL_SHORTCUT)) {
			name = context.getString(R.string.permlab_installShortcut);
		} else if (name.equals(UNINSTALL_SHORTCUT)) {
			name = context.getString(R.string.permlab_uninstallShortcut);
		} else if (name.equals(C2DM_RECEIVE)) {
			name = context.getString(R.string.permlab_c2dmReceive);
		} else {
			// Log.e("Unknown permission", name);
		}

		if (!name.equals(permission)) {
			name = name.substring(0, 1).toUpperCase(Locale.getDefault())
					+ name.substring(1);

		}

		return name;
	}
}
