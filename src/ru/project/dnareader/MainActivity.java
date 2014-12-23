package ru.project.dnareader;

import java.io.File;
import java.io.IOException;

import org.biojava.bio.program.abi.ABITrace;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.AtomicSymbol;
import org.biojava.bio.symbol.IllegalSymbolException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btn;
	// String filePath = null;
	static TextView tv1;
	static TextView tv2;
	final boolean DEBUG = true;

	// public static String filePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// btn = (Button) findViewById(R.id.btn);
		tv1 = (TextView) findViewById(R.id.textView1);

		tv2 = (TextView) findViewById(R.id.textView2);
		// btn.setOnClickListener(this);

		if (DEBUG) {
			try {
				continuee("/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1");
			} catch (IllegalSymbolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

	public static void continuee(String ABIfilePath) throws IOException,
			IllegalSymbolException { // эта
		// функция
		// работает
		// с
		// jni
		// после
		// закрытия диалогового окна выбора
		// файла

		// tv2.setText("status: " + WorkingWithJni.stringFromJNI(ABIfilePath));
		// Log.v("TAG", ABIfilePath);
		File ABIfile = new File(ABIfilePath);
		ABITrace ABITrace = new ABITrace(ABIfile);
		int[] array;
		AtomicSymbol a = DNATools.a();

		array = ABITrace.getTrace(a);
		for (int i = 0; i < array.length; i++)
			Log.d("INT", "array " + array[i]);
		// Toast.makeText(null, "everithing is ok", Toast.LENGTH_LONG).show();

	}

}
