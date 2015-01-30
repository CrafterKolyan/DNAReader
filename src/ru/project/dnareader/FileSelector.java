package ru.project.dnareader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FileSelector {

	private ListView mFileListView;

	private File mCurrentLocation;

	public static String finalItimePath;

	private static final String TAG = "DnaReader";

	private final Dialog mDialog;

	private String[] fileFilters;

	private Context mContext;
	private Graphic graphView;

	public FileSelector(final Context context, final String[] fileFilters,
			Graphic graphView) {
		this.fileFilters = fileFilters;

		this.graphView = graphView;

		mContext = context;
		mCurrentLocation = Environment.getExternalStorageDirectory();

		mDialog = new Dialog(context);
		mDialog.setContentView(R.layout.dialog);
		mDialog.setTitle(mCurrentLocation.getAbsolutePath());
	}

	public void showDialogs() {

		prepareFilesList();
		show();

	}

	private void prepareFilesList() {
		mFileListView = (ListView) mDialog.findViewById(R.id.fileList);

		mFileListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				if (id == 0) {
					final String parentLocation = mCurrentLocation.getParent();
					if (parentLocation != null) { // text == "../"
						mCurrentLocation = new File(parentLocation);
						makeList(mCurrentLocation);
					} else {
						onItemSelect(parent, position);
					}
				} else {
					onItemSelect(parent, position);
				}
			}
		});
		makeList(mCurrentLocation);
	}

	private boolean campareformat(File tempFile) {
		for (int i = 0; i < fileFilters.length; i++)
			if (FileUtils.accept(tempFile, fileFilters[i]) == true)
				return true;
		return false;
	}

	private void makeList(final File location) {
		final ArrayList<FileData> fileList = new ArrayList<FileData>();
		final String parentLocation = location.getParent();
		if (parentLocation != null) {
			fileList.add(new FileData("../", FileData.UP_FOLDER));
		}
		File listFiles[] = location.listFiles();
		if (listFiles != null) {
			ArrayList<FileData> fileDataList = new ArrayList<FileData>();
			for (int index = 0; index < listFiles.length; index++) {
				File tempFile = listFiles[index];
				if (campareformat(tempFile)) {
					int type = tempFile.isDirectory() ? FileData.DIRECTORY
							: FileData.FILE;
					fileDataList.add(new FileData(listFiles[index].getName(),
							type));
				}
			}
			fileList.addAll(fileDataList);
			Collections.sort(fileList);
		}
		// Fill the list with the contents of fileList.
		if (mFileListView != null) {
			FileListAdapter adapter = new FileListAdapter(mContext, fileList);
			mFileListView.setAdapter(adapter);
		}
	}

	private void onItemSelect(final AdapterView<?> parent, final int position) {
		final String itemText = ((FileData) parent.getItemAtPosition(position))
				.getFileName();
		final String itemPath = mCurrentLocation.getAbsolutePath()
				+ File.separator + itemText;
		final File itemLocation = new File(itemPath);

		if (!itemLocation.canRead()) {
			Toast.makeText(mContext, "Access denied!!!", Toast.LENGTH_SHORT)
					.show();
		} else if (itemLocation.isDirectory()) {
			mCurrentLocation = itemLocation;
			makeList(mCurrentLocation);
		} else if (itemLocation.isFile()) {
			finalItimePath = itemPath;
			dismiss();
		}
	}

	public File getCurrentLocation() {
		return mCurrentLocation;
	}

	public void show() {
		mDialog.show();
	}

	public String path() {
		return finalItimePath;
	}

	public void dismiss() {
		mDialog.dismiss();

		Log.v(TAG, "FileSelector parh: " + finalItimePath);

		graphView.newData(new File(finalItimePath));

		graphView.graphTo(0);

		new Download(mContext, finalItimePath);

	}
}
