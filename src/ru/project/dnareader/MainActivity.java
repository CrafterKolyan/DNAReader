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

public class MainActivity extends Activity {

	Button btn;
	static TextView tv1;
	static TextView tv2;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(new graphic(this, null, null, null, null));

		setContentView(R.layout.main);

		// btn = new Button;
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// graph2.drawThread.setRunning(true);
				// graph2.drawThread.start();

				try {
					File abifile = new File(
							"/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1");
					ABITrace abiTrace = new ABITrace(abifile);
					graph2.traceA = abiTrace.getTrace(DNATools.a());
					graph2.drawThread.setRunning(true);
					graph2.drawThread.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalSymbolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

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

	public static void continuee(String ABIfilePath) {

	}

}
