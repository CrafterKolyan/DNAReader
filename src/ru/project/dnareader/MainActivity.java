package ru.project.dnareader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btn1;
	Button btn2;
	boolean status = false;
	Context context;
	private static final String TAG = "DnaReader";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// ((Graphic) findViewById(R.id.mySurface)).newData(new File(
		// "/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1"));

		// ((Graphic) findViewById(R.id.mySurface)).newData(new File(
		// "/storage/emulated/0/Download/2.abi"));

		btn1 = (Button) findViewById(R.id.button1);

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Download();
			}
		});

		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(new View.OnClickListener() {

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
				mFileFilter, (Graphic) findViewById(R.id.mySurface));
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
