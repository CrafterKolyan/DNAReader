package ru.project.dnareader;

import java.io.File;
import java.io.IOException;

import org.biojava.bio.program.abi.ABITrace;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalSymbolException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btn;
	Button btn1;
	static TextView tv1;
	static TextView tv2;
	boolean status = false;
	Context context;
	View qw = null;
	private static final String TAG = "DnaReader";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		btn = (Button) findViewById(R.id.button1);

		qw = (View) findViewById(R.id.mySurface);

		if (qw == null)
			// Log.v(TAG, "qw == null");
			Toast.makeText(this, "qw == null", Toast.LENGTH_LONG).show();
		else
			// // Log.v(TAG, "qw != null");
			Toast.makeText(this, "qw != null", Toast.LENGTH_LONG).show();

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Graphic.checkHeightRate = true;

				try {
					File abifile = new File(
							"/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1");

					ABITrace abiTrace = new ABITrace(abifile);
					Graphic.secA.trace(abiTrace.getTrace(DNATools.a()));
					Graphic.secC.trace(abiTrace.getTrace(DNATools.c()));
					Graphic.secG.trace(abiTrace.getTrace(DNATools.g()));
					Graphic.secT.trace(abiTrace.getTrace(DNATools.t()));

					Graphic.baseCallsX = abiTrace.getBasecalls();
					Graphic.baseCallsLetters = abiTrace.getSequence()
							.seqString();

					Graphic.isDrawing = true;

					// qw.invalidate();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalSymbolException e) {
					e.printStackTrace();
				}
			}

		});

		btn1 = (Button) findViewById(R.id.button123);
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Graphic.graphstart = 0;
			}
		});

	}

	// @Override
	// public View onCreateView(View parent, String name, Context context,
	// AttributeSet attrs) {
	// // TODO Auto-generated method stub
	// qw = (View) findViewById(R.id.mySurface);
	//
	// if (qw == null)
	// Log.v(TAG, "qw == null");
	// // Toast.makeText(this, "qw == null", Toast.LENGTH_LONG).show();
	// else
	// Log.v(TAG, "qw != null");
	// // Toast.makeText(this, "qw != null", Toast.LENGTH_LONG).show();
	//
	// return super.onCreateView(parent, name, context, attrs);
	// }

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
				mFileFilter);
		fileSelector.showDialogs();
	}

	public static void continuee(String ABIfilePath) {

	}

}
