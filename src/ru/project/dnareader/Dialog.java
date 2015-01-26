package ru.project.dnareader;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.app.Dialog;

public class Dialog extends Activity {

	final static String LOG_TAG = "myLogs";
	final int DIALOG = 1;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onclick(View v) {
		showDialog(DIALOG);
	}

	// protected android.app.Dialog onCreateDialog(int id) {
	// Log.d(LOG_TAG, "onCreateDialog");
	// if (id == DIALOG) {
	// AlertDialog.Builder adb = new AlertDialog.Builder(this);
	// adb.setTitle("Текущее время");
	// adb.setMessage(sdf.format(new Date(System.currentTimeMillis())));
	// return adb.create();
	// }
	// // return super.onCreateDialog(id);
	// }

	protected void onPrepareDialog(int id, android.app.Dialog dialog) {
		// super.onPrepareDialog(id, dialog);
		Log.d(LOG_TAG, "onPrepareDialog");
		if (id == DIALOG) {
			((AlertDialog) dialog).setMessage(sdf.format(new Date(System
					.currentTimeMillis())));
		}
	}
}

/*
 * package ru.startandroid.develop.p0611alertdialogprepare;
 * 
 * import java.sql.Date; import java.text.SimpleDateFormat;
 * 
 * import android.app.Activity; import android.app.AlertDialog; import
 * android.app.Dialog; import android.os.Bundle; import android.util.Log; import
 * android.view.View;
 * 
 * public class MainActivity extends Activity {
 * 
 * final static String LOG_TAG = "myLogs"; final int DIALOG = 1;
 * SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
 * 
 * // Called when the activity is first created.
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.main); }
 * 
 * public void onclick(View v) { showDialog(DIALOG); }
 * 
 * protected Dialog onCreateDialog(int id) { Log.d(LOG_TAG, "onCreateDialog");
 * if (id == DIALOG) { AlertDialog.Builder adb = new AlertDialog.Builder(this);
 * adb.setTitle("Текущее время"); adb.setMessage(sdf.format(new
 * Date(System.currentTimeMillis()))); return adb.create(); } return
 * super.onCreateDialog(id); }
 * 
 * protected void onPrepareDialog(int id, Dialog dialog) {
 * super.onPrepareDialog(id, dialog); Log.d(LOG_TAG, "onPrepareDialog"); if (id
 * == DIALOG) { ((AlertDialog) dialog).setMessage(sdf.format(new Date(System
 * .currentTimeMillis()))); } } }
 */