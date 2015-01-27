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

//import android.widget.Spinner;

/**
 * Create the file selection dialog. This class will create a custom dialog for
 * file selection which can be used to save files.
 */
public class FileSelector {

	/** The list of files and folders which you can choose from */
	private ListView mFileListView;

	/**
	 * Indicates current location in the directory structure displayed in the
	 * dialog.
	 */
	private File mCurrentLocation;

	private String finalItimePath;

	private static final String TAG = "DnaReader";

	/**
	 * The file selector dialog.
	 */
	private final Dialog mDialog;

	private String[] fileFilters;

	private Context mContext;
	private Graphic graphView;

	/**
	 * Constructor that creates the file selector dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param operation
	 *            LOAD - to load file / SAVE - to save file
	 * @param onHandleFileListener
	 *            Notified after pressing the save or load button.
	 * @param fileFilters
	 *            Array with filters
	 */
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

	/**
	 * This method prepares the mFileListView
	 * 
	 */
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

	/**
	 * The method that fills the list with a directories contents.
	 * 
	 * @param location
	 *            Indicates the directory whose contents should be displayed in
	 *            the dialog.
	 * @param fitlesFilter
	 *            The filter specifies the type of file to be displayed
	 */
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

	/**
	 * Handle the file list item selection.
	 * 
	 * Change the directory on the list or change the name of the saved file if
	 * the user selected a file.
	 * 
	 * @param parent
	 *            First parameter of the onItemClick() method of
	 *            OnItemClickListener. It's a value of text property of the
	 *            item.
	 * @param position
	 *            Third parameter of the onItemClick() method of
	 *            OnItemClickListener. It's the index on the list of the
	 *            selected item.
	 */
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
			Toast.makeText(mContext, itemPath, Toast.LENGTH_SHORT).show();
			finalItimePath = itemPath;
			dismiss();
		}
	}

	public File getCurrentLocation() {
		return mCurrentLocation;
	}

	/** Simple wrapper around the Dialog.show() method. */
	public void show() {
		mDialog.show();
	}

	public String path() {
		return finalItimePath;
	}

	/** Simple wrapper around the Dialog.dissmiss() method. */
	public void dismiss() {
		mDialog.dismiss();

		Log.v(TAG, "FileSelector parh: " + finalItimePath);

		graphView.newData(new File(finalItimePath));

	}
}
