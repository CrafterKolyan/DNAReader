package ru.project.dnareader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;

//import android.app.ProgressDialog;

public class Download extends Activity {

	public Download() {
		startDownload();
	}

	// private Button startBtn;
	//
	// // private ProgressDialog mProgressDialog;
	//
	// /** Called when the activity is first created. */
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// // setContentView(R.layout.main2);
	// startBtn = (Button) findViewById(R.id.startBtn);
	// startBtn.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	//
	// }
	// });
	// }

	private void startDownload() {
		String url = "http://skib6.ru:21180/dna/info/1";
		new DownloadFileAsync().execute(url);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		// @SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						"/storage/emulated/0/Download/2.abi");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
			}
			return null;

		}

		// protected void onProgressUpdate(String... progress) {
		// Log.d("ANDRO_ASYNC", progress[0]);
		// // mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		// }

		@Override
		protected void onPostExecute(String unused) {
			// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			// Toast.makeText(this, "downloaded", Toast.LENGTH_LONG).show();
		}
	}
}