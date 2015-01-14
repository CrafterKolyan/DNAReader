package ru.project.dnareader;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btn;
	Button btn1;
	// static TextView tv1;
	// static TextView tv2;
	boolean status = false;
	Context context;
	Graphic myView = null;
	private static final String TAG = "DnaReader";

	static TextView tv1;
	static TextView tv2;
	static TextView tv3;
	static TextView tv4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// tv1 = (TextView) findViewById(R.id.textView1);
		// tv2 = (TextView) findViewById(R.id.textView2);
		btn = (Button) findViewById(R.id.button1);

		myView = (Graphic) findViewById(R.id.mySurface);

		myView.newData(new File(
				"/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1"));

		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				((Graphic) findViewById(R.id.mySurface))
						.newData(new File(
								"/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1"));

			}

		});

		btn1 = (Button) findViewById(R.id.button123);
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((Graphic) findViewById(R.id.mySurface)).graphToBegin();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.loadFile:
			showFileSelector();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void showFileSelector() {

		/** Sample filters array */
		final String[] mFileFilter = { ".ab1", ".abi" };

		final FileSelector fileSelector = new FileSelector(MainActivity.this,
				mFileFilter, myView);
		fileSelector.showDialogs();
	}

	public static void continuee(String ABIfilePath) {

	}

	public void setText(final TextView text, final String value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				text.setText(value);
			}
		});
	}

}
