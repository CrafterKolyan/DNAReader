package ru.project.dnareader;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btn1;
	Button btn2;
	boolean status = false;
	Context context;
	private static final String TAG = "DnaReader";
	public static FragmentManager fragManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		fragManager = getFragmentManager();

		showFileSelector();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.loadFile:
			showFileSelector();
			break;
		case R.id.blastSearch:
			new Download("http://skib6.ru:21180/dna/info/1",
					"/storage/emulated/0/DNAreader/1234567.txt",
					MainActivity.this);
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

}

// ((Graphic) findViewById(R.id.mySurface))
// .newData(new File(
// "/storage/emulated/0/Download/GJA1-19-II-1-ex1_GJA1-1F_A2.ab1"));

// ((Button) findViewById(R.id.download))
// .setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View v) {
// new Download("http://skib6.ru:21180/dna/info/1",
// "/storage/emulated/0/DNAreader/1234567.txt",
// MainActivity.this);
// // new Download(
// //
// "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?QUERY=ACCACAGTCGGAGATAATAGGACGAAGTAANACTGACGNGATACTTTCCCGAGCTGAAGTTAACAAATGCACCTGGTTCTTTTACTAAGTGTTCAAATACCAGTGAACTTAAAGAATTTGTCAATCCTAGCCTTCCAAGAGAAGAAAAAGAAGAGAAACTAGAAACAGTTAAAGTGTCTAATAATGCTGAAGACCCCAAAGATCTCATGTTAAGTGGAGAAAGGGTTTTGCAAACTGAAAGATCTGTAGAGAGTAGCAGTATTTCATTGGTACCTGGTACTGATTATGGCACTCAGGAAAGTATCTCGTTACTGGAAGTTAGCACTCTAGGGAAGGCAAAAACAGAACCAAATAAATGTGTGAGTCAGTGTGCAGCATTTGAAAACCCCAAGGGACTAATTCATGGTTGTTCCAAAGATAATAGAAATGACACAGAAGGCTTTAAGTATCCATTGGGACATGAAGTTAACCACAGTCGGGAAACAAGCATAGAAATGGAAGAAAGTGAACTTGATGCTCAGTATTTGCAGAATACATTCAAGGTTTCAAAGCGCCAGTCATTTGCTCTGTTTTCAAATCCAGGAAATGCAGAAGAGGAATGTGCAACATTCTCTGCCCACGTCATCGCTGGCCCCTTGCGAAGAGGATATTCTACGGATCGTAATCG&DATABASE=nr&PROGRAM=blastn&FILTER=L&EXPECT=0.01&FORMAT_TYPE=XML&NCBI_GI=on&HITLIST_SIZE=10&CMD=Put",
// // "/storage/emulated/0/DNAreader/1234567.txt");
// }
// });
//
// ((Button) findViewById(R.id.toBegin))
// .setOnClickListener(new View.OnClickListener() {
//
// @Override
// public void onClick(View v) {
// ((Graphic) findViewById(R.id.mySurface)).graphToBegin();
//
// }
// });
// ((Button) findViewById(R.id.dialog))
// .setOnClickListener(new View.OnClickListener() {
//
// @Override
// public void onClick(View arg0) {
// (new MyDialog("все очень плохо")).show(fragManager, "");
//
// }
// });

// btn1.setAlpha(0);
// btn2.setAlpha(0);
