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

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class Download extends Activity {

	private String filePath;
	private static final String TAG = "DnaReader";

	public Download(String url, String filePath) {
		// String url = "http://skib6.ru:21180/dna/info/1";
		// "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi?QUERY=ACCACAGTCGGAGATAATAGGACGAAGTAANACTGACGNGATACTTTCCCGAGCTGAAGTTAACAAATGCACCTGGTTCTTTTACTAAGTGTTCAAATACCAGTGAACTTAAAGAATTTGTCAATCCTAGCCTTCCAAGAGAAGAAAAAGAAGAGAAACTAGAAACAGTTAAAGTGTCTAATAATGCTGAAGACCCCAAAGATCTCATGTTAAGTGGAGAAAGGGTTTTGCAAACTGAAAGATCTGTAGAGAGTAGCAGTATTTCATTGGTACCTGGTACTGATTATGGCACTCAGGAAAGTATCTCGTTACTGGAAGTTAGCACTCTAGGGAAGGCAAAAACAGAACCAAATAAATGTGTGAGTCAGTGTGCAGCATTTGAAAACCCCAAGGGACTAATTCATGGTTGTTCCAAAGATAATAGAAATGACACAGAAGGCTTTAAGTATCCATTGGGACATGAAGTTAACCACAGTCGGGAAACAAGCATAGAAATGGAAGAAAGTGAACTTGATGCTCAGTATTTGCAGAATACATTCAAGGTTTCAAAGCGCCAGTCATTTGCTCTGTTTTCAAATCCAGGAAATGCAGAAGAGGAATGTGCAACATTCTCTGCCCACGTCATCGCTGGCCCCTTGCGAAGAGGATATTCTACGGATCGTAATCG&DATABASE=nr&PROGRAM=blastn&FILTER=L&EXPECT=0.01&FORMAT_TYPE=XML&NCBI_GI=on&HITLIST_SIZE=10&CMD=Put";
		this.filePath = filePath;

		new DownloadFileAsync().execute(url);
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

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
				// Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(filePath);

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

			}
			return null;

		}

		private void readingFile() {
			Log.v(TAG, "Download readingFile ");
			try {
				Scanner in;
				in = new Scanner(new File(filePath));
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
				}
				in.close();

			} catch (FileNotFoundException e) {
				Log.v(TAG, "Exception ", e);
			}

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d(TAG, "Download onProgressUpdate" + progress[0]);
			// Log.d(TAG, "Download " + progress[1]);
			// Log.d(TAG, "Download " + progress[2]);
			// mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			Log.d(TAG, "Download onPostExecute ");
			// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			// Toast.makeText(this, "downloaded", Toast.LENGTH_LONG).show();
		}
	}
}