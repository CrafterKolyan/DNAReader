package ru.project.dnareader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btn;
	// String filePath = null;
	static TextView tv1;
	static TextView tv2;
	public static String filePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// btn = (Button) findViewById(R.id.btn);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		// btn.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.loadFile:
			showFileSelector();
		default:
			return super.onOptionsItemSelected(item);
		}
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
				mFileFilter);
		fileSelector.showDialogs();
	}

	public static void continuee() { // эта функция работает с jni после
										// закрытия диалогового окна

		tv2.setText("status: " + WorkingWithJni.stringFromJNI(filePath));

	}

}
