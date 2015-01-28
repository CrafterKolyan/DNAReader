package ru.project.dnareader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Download extends Activity {

	private String mFilePath;
	private static final String TAG = "DnaReader";
	private Context mContext = null;
	boolean bool = false;
	public static Vector<Mutation> mutations = null;

	public Download(String url, String filePath, Context context) {
		// String url = "http://skib6.ru:21180/dna/info/1";
		// "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?QUERY=ACCACAGTCGGAGATAATAGGACGAAGTAANACTGACGNGATACTTTCCCGAGCTGAAGTTAACAAATGCACCTGGTTCTTTTACTAAGTGTTCAAATACCAGTGAACTTAAAGAATTTGTCAATCCTAGCCTTCCAAGAGAAGAAAAAGAAGAGAAACTAGAAACAGTTAAAGTGTCTAATAATGCTGAAGACCCCAAAGATCTCATGTTAAGTGGAGAAAGGGTTTTGCAAACTGAAAGATCTGTAGAGAGTAGCAGTATTTCATTGGTACCTGGTACTGATTATGGCACTCAGGAAAGTATCTCGTTACTGGAAGTTAGCACTCTAGGGAAGGCAAAAACAGAACCAAATAAATGTGTGAGTCAGTGTGCAGCATTTGAAAACCCCAAGGGACTAATTCATGGTTGTTCCAAAGATAATAGAAATGACACAGAAGGCTTTAAGTATCCATTGGGACATGAAGTTAACCACAGTCGGGAAACAAGCATAGAAATGGAAGAAAGTGAACTTGATGCTCAGTATTTGCAGAATACATTCAAGGTTTCAAAGCGCCAGTCATTTGCTCTGTTTTCAAATCCAGGAAATGCAGAAGAGGAATGTGCAACATTCTCTGCCCACGTCATCGCTGGCCCCTTGCGAAGAGGATATTCTACGGATCGTAATCG&DATABASE=nr&PROGRAM=blastn&FILTER=L&EXPECT=0.01&FORMAT_TYPE=XML&NCBI_GI=on&HITLIST_SIZE=10&CMD=Put";
		this.mFilePath = filePath;
		this.mContext = context;
		mutations = new Vector<Mutation>();
		new DownloadFileAsync().execute(url);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				Log.d(TAG, "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(mFilePath);

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

				readingFile();

			} catch (Exception e) {
				Log.v(TAG, "Download Exception ", e);
				bool = true;
			}
			return null;

		}

		private void readingFile() {
			Log.v(TAG, "Download readingFile ");
			try {
				Scanner in;
				in = new Scanner(new File(mFilePath));

				while (in.hasNext()) {
					String s = "";
					String s1 = "";
					String s2 = "";
					int i = 0;

					s += in.nextLine() + "\r\n";
					i = s.indexOf(':');
					s1 = s.substring(0, i++);
					s2 = s.substring(i, s.length());
					Log.v(TAG, "номер нуклеотида " + s1 + "\n     мутация "
							+ s2);

					mutations.add(new Mutation(Graphic.mBaseCallsX[Integer
							.parseInt(s1)], s2));

				}
				in.close();

			} catch (FileNotFoundException e) {
				Log.e(TAG, "Exception ", e);
			}
			// mutations.add(new Mutation(20, 20, 20, "s2"));
			// mutations.add(new Mutation(20, 20, 50, "s2"));
			// mutations.add(new Mutation(20, 20, 77, "s2"));
			// mutations.add(new Mutation(20, 20, 100, "s2"));

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d(TAG, "Download onProgressUpdate" + progress[0]);
		}

		@Override
		protected void onPostExecute(String unused) {
			Log.d(TAG, "Download onPostExecute ");
			if (bool)
				Toast.makeText(mContext,
						"Ошибка загрузки, возможно проблема с интернетом",
						Toast.LENGTH_LONG).show();
		}
	}
}