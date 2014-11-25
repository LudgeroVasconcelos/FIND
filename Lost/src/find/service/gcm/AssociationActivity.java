package find.service.gcm;

import find.service.R;
import find.service.net.diogomarques.wifioppish.MessagesProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class AssociationActivity extends Activity {
	private Context c;
	private final int threshold = (60 * 2 * 1000);
	private final static String TAG = "gcm";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		Intent intent = getIntent();
		if (intent!=null && intent.getAction()!=null && intent.getAction().equals("disassociate")) {
			ScheduleService.cancelAlarm(this);
			Simulation.regSimulationContentProvider("", "", "",
					"", this);
			Log.d(TAG, "canceling alarm via notification");
			finish();
			return;
		}

		final String name = intent.getExtras().getString("name");
		final String location = intent.getExtras().getString("location");
		final String date = intent.getExtras().getString("date");
		final String duration = intent.getExtras().getString("duration");

		final double latS = intent.getExtras().getDouble("latS");
		final double lonS = intent.getExtras().getDouble("lonS");
		final double latE = intent.getExtras().getDouble("latE");
		final double lonE = intent.getExtras().getDouble("lonE");

		// set timer for retriving location
		/*
		 * long timeleft = DateFunctions.timeToDate(date); long handlerTimer =
		 * timeleft - threshold; if (handlerTimer < 0) handlerTimer = 0; else {
		 * if (handlerTimer > threshold) handlerTimer = threshold; }
		 */

		// Log.d(TAG, "pop up timer:" + handlerTimer);
		Simulation.preDownloadTiles(latS, lonS, latE, lonE, c);
		ScheduleService.setStartAlarm(date, c);
		Intent disassociate = new Intent(this,
				AssociationActivity.class);
		disassociate.setAction("disassociate");
		Notifications.generateNotification(c, "Associate to "
				+ name , "Click to disassociate.",disassociate );
		Simulation.regSimulationContentProvider(name, date, duration, location,
				c);
		
		finish();	
	

	}

}