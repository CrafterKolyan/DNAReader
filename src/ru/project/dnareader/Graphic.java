package ru.project.dnareader;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.biojava.bio.program.abi.ABITrace;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.AtomicSymbol;
import org.biojava.bio.symbol.IllegalSymbolException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Graphic extends SurfaceView implements SurfaceHolder.Callback {

	private DrawThread mDrawThread;
	private ScrollThread mScrollThread;
	private static final String TAG = "DnaReader";
	private boolean mScrollDirection;// true-палец двигался с права на лево или
										// наоборот
	private boolean mScrollThreadCheck = false;
	private float mSpeed;
	private float staticX = 0;
	private float staticDist = 0;
	private float mGraphstart = 0;
	private float mGraphWidth = 0;
	private float[] mPreviousTouches = new float[5];
	private long[] mPreviousTime = new long[10];
	private long mPreviousTime1 = 0;
	private float mPreviousTouchX1 = 0;
	private float mPrevTouchY = 0;
	private long mMaxTime = 300;
	private float mMaxDistant = 30;
	private int[] mBaseCallsX = null;
	private char[] mBaseCallsLetters = null;
	private boolean mIsDrawing = false;
	private Sequence mSecA = new Sequence(DNATools.a());
	private Sequence mSecC = new Sequence(DNATools.c());
	private Sequence mSecG = new Sequence(DNATools.g());
	private Sequence mSecT = new Sequence(DNATools.t());
	private Canvas mCanvas = null;
	private boolean mCheckHeightRate = true;
	private boolean mDrag = false;
	private boolean mSwype = false;
	private float mDragX = 0;
	private float mMaxGraphicHeigt = 0;
	private float mGraphHeightRate = 1;
	private float mRealhHeightRate = 1;
	private float mRealhHeightRateTemp = 1;
	private float mRealhWidthRate = 10;
	private float mRealhWidthRateTemp = 10;
	private float mDiffrentX = 0;
	private float mDiffrentY = 0;
	private float mCanvasWidth = 0;
	private float mCanvasHeight = 0;
	private int[] mDoublePeaks = null;
	private int mExtraPartOfGraphic = 50;

	@SuppressWarnings("unused")
	private float mDragY = 0;

	public Graphic(Context context) {
		super(context);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		Log.v(TAG, "Graphic Graphic 1");
		getHolder().addCallback(this);
	}

	public Graphic(Context context, AttributeSet attrs) {
		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		Log.v(TAG, "Graphic Graphic 2");
		getHolder().addCallback(this);
	}

	public Graphic(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		Log.v(TAG, "Graphic Graphic 3");
		getHolder().addCallback(this);
	}

	public void newData(File file) {

		Log.v(TAG, "Graphic newData");

		try {

			ABITrace abiTrace = new ABITrace(file);
			mSecA.trace(abiTrace.getTrace(DNATools.a()));
			mSecC.trace(abiTrace.getTrace(DNATools.c()));
			mSecG.trace(abiTrace.getTrace(DNATools.g()));
			mSecT.trace(abiTrace.getTrace(DNATools.t()));
			mBaseCallsX = abiTrace.getBasecalls();
			mBaseCallsLetters = abiTrace.getSequence().seqString()
					.toCharArray();
			mExtraPartOfGraphic = mSecC.trace.length
					- mBaseCallsX[mBaseCallsX.length - 1] - 40;
			mDoublePeaks = doublePeaks();
			Log.v(TAG, "base: " + mBaseCallsX[mBaseCallsX.length - 1]);

		} catch (IOException e) {
			// e.printStackTrace();
			// Log.v(TAG, "everything is bad");
			Log.v(TAG, e.getMessage());
			Log.v(TAG, "Exception ", e);
			return;
		} catch (IllegalSymbolException e) {
			// e.printStackTrace();
			// Log.v(TAG, "everything is bad");
			// e.getMessage();
			Log.v(TAG, e.getMessage());
			Log.v(TAG, "Exception ", e);
			return;
		}
		mIsDrawing = true;
		mCheckHeightRate = true;

	}

	private int[] doublePeaks() {
		Vector<Integer> vec = new Vector<Integer>();
		Integer[] vec1 = null;
		int[] vec2;

		for (int i = 0; i < mBaseCallsX.length; i++) {
			if ((Max(true, mBaseCallsX[i]) / Max(false, mBaseCallsX[i])) > 0.5) {
				vec.add(Integer.valueOf(mBaseCallsX[i]));
			}
		}
		vec1 = new Integer[vec.size()];

		vec.toArray(vec1);
		vec2 = new int[vec.size()];

		for (int i = 0; i < vec.size(); i++)
			vec2[i] = vec1[i].intValue();

		return vec2;
	}

	private float Max(boolean bool, int x) {
		float[] arr = { mSecA.trace[x], mSecC.trace[x], mSecG.trace[x],
				mSecT.trace[x] };
		float max1 = 0;
		float max2 = 0;

		for (int i = 0; i < arr.length; i++) {
			if (max1 < arr[i])
				max1 = arr[i];
		}

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < max1 && max2 < arr[i])
				max2 = arr[i];
		}

		if (bool)
			return max2;
		else
			return max1;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mCheckHeightRate = true;
		Log.v(TAG, "Graphic surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		mScrollThread = new ScrollThread();
		mScrollThread.start();
		mDrawThread = new DrawThread(getHolder());
		Log.v(TAG, "Graphic surfaceCreated");

		mDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(TAG, "Graphic surfaceDestroyed");
		mDrawThread.interrupt();
		try {
			mDrawThread.join();
		} catch (InterruptedException e) {
			Log.v(TAG, e.getMessage());
			Log.v(TAG, "Exception ", e);
		}
	}

	class DrawThread extends Thread {

		private SurfaceHolder surfaceHolder;

		public DrawThread(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
			Log.v(TAG, "Graphic DrawThread");
		}

		private void drawingGraph(int[] trace, AtomicSymbol base) {
			Path path = new Path();
			path.reset();
			path.moveTo(mGraphstart, mCanvasHeight - trace[0]
					* mRealhHeightRate);

			int beggining = Math.abs((int) (mGraphstart / mRealhWidthRate));
			int ending = beggining + (int) (mCanvasWidth / mRealhWidthRate) + 3;

			if (ending > trace.length - mExtraPartOfGraphic)
				ending = trace.length - mExtraPartOfGraphic;

			for (int i = beggining; i < ending; i += 1) {
				if ((mCanvasHeight - trace[i] * mRealhHeightRate) < 50) {

					if (base == DNATools.a()) {
						path.lineTo(mGraphstart + i * mRealhWidthRate, 57);
					} else if (base == DNATools.c()) {
						path.lineTo(mGraphstart + i * mRealhWidthRate, 53);
					} else if (base == DNATools.g()) {
						path.lineTo(mGraphstart + i * mRealhWidthRate, 49);
					} else if (base == DNATools.t()) {
						path.lineTo(mGraphstart + i * mRealhWidthRate, 45);
					}

				} else
					path.lineTo(mGraphstart + i * mRealhWidthRate,
							mCanvasHeight - trace[i] * mRealhHeightRate);
			}

			if (base == DNATools.a()) {
				mSecA.path = path;
			} else if (base == DNATools.c()) {
				mSecC.path = path;
			} else if (base == DNATools.g()) {
				mSecG.path = path;
			} else if (base == DNATools.t()) {
				mSecT.path = path;
			}

		}

		private void drawPeaks() {
			Paint paint = new Paint();
			paint.setColor(Color.parseColor("#40FF0000"));

			for (int i = 0; i < mDoublePeaks.length; i++) {
				mCanvas.drawRect(mGraphstart + mDoublePeaks[i]
						* mRealhWidthRate - 15, 50, mGraphstart
						+ mDoublePeaks[i] * mRealhWidthRate + 15,
						mCanvasHeight, paint);
			}
		}

		private void drawSymbols() {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.YELLOW);

			for (int i = 0; i < mBaseCallsLetters.length; i++) {
				mBaseCallsLetters[i] = Character
						.toUpperCase(mBaseCallsLetters[i]);

			}

			int begging = Math.abs((int) (mGraphstart / mRealhWidthRate)) - 20;
			int end = (int) (begging + mCanvasWidth / mRealhWidthRate) + 20;

			for (int i = 0; i < mBaseCallsLetters.length; i++) {
				if (mBaseCallsX[i] > begging && mBaseCallsX[i] < end) {
					paint.reset();
					paint.setTextSize(50);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					switch (mBaseCallsLetters[i]) {
					case 'A':
						paint.setColor(Color.GREEN);
						break;
					case 'C':
						paint.setColor(Color.BLUE);
						break;
					case 'G':
						paint.setColor(Color.BLACK);
						break;
					case 'T':
						paint.setColor(Color.RED);
						break;
					}

					mCanvas.drawText(mBaseCallsLetters, i, 1, mGraphstart
							+ (float) (mBaseCallsX[i]) * mRealhWidthRate - 15,
							40, paint);

				} else if (mBaseCallsLetters[i] >= end)
					return;
			}

		}

		@Override
		public void run() {

			Log.v(TAG, "Graphic run");

			while (!mDrawThread.isInterrupted()) {

				mCanvas = null;

				try {
					mCanvas = surfaceHolder.lockCanvas(null);

					if (mCanvas == null || mGraphHeightRate == 0)
						return;

					mCanvas.drawColor(Color.WHITE);

					if (mIsDrawing) {
						if (mSecA == null)
							Log.v(TAG, "secA == null");

						mGraphWidth = (mSecA.trace.length - mExtraPartOfGraphic)
								* mRealhWidthRate;

						if (mCheckHeightRate) {
							mMaxGraphicHeigt = 0;
							mCanvasWidth = mCanvas.getWidth();
							mCanvasHeight = mCanvas.getHeight() - 10;
							mMaxGraphicHeigt = Math.max(
									Math.max(mSecA.max, mSecC.max),
									Math.max(mSecG.max, mSecT.max));
							mGraphHeightRate = (mCanvasHeight - 40)
									/ mMaxGraphicHeigt;
							mCheckHeightRate = false;
							mRealhHeightRate = mGraphHeightRate;
							mRealhHeightRateTemp = mGraphHeightRate;
						}

						drawSymbols();

						drawPeaks();

						drawingGraph(mSecA.trace, DNATools.a());
						drawingGraph(mSecC.trace, DNATools.c());
						drawingGraph(mSecG.trace, DNATools.g());
						drawingGraph(mSecT.trace, DNATools.t());

						mCanvas.drawPath(mSecA.path, mSecA.paint);
						mCanvas.drawPath(mSecC.path, mSecC.paint);
						mCanvas.drawPath(mSecG.path, mSecG.paint);
						mCanvas.drawPath(mSecT.path, mSecT.paint);

					}
				} finally {
					if (mCanvas != null) {
						surfaceHolder.unlockCanvasAndPost(mCanvas);
					}
				}
			}
		}
	}

	class ScrollThread extends Thread {

		public ScrollThread() {

		}

		@Override
		public void run() {
			while (true) {
				if (mScrollThread.isInterrupted())
					return;

				while (mScrollThreadCheck) {
					if (mSpeed < 0) {
						mScrollThreadCheck = false;
						break;
					}
					mSpeed -= 0.02;

					if (mScrollDirection) {
						mGraphstart -= mSpeed;
					} else {
						mGraphstart += mSpeed;
					}

					if (mGraphstart > 0) {
						mScrollThreadCheck = false;
						break;
					}

					if (mGraphstart + mGraphWidth < mCanvasWidth) {
						mScrollThreadCheck = false;
						break;
					}

					try {
						TimeUnit.MILLISECONDS.sleep(1);
					} catch (InterruptedException e) {
						Log.v(TAG, e.getMessage());
						Log.v(TAG, "Exception ", e);
					}
				}
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float evX1 = 0;
		int actionMask = event.getActionMasked();
		int pointerCount = event.getPointerCount();

		switch (actionMask) {
		case MotionEvent.ACTION_DOWN:

			mScrollThreadCheck = false;
			mDrag = true;
			evX1 = event.getX(0);
			mDragX = evX1 - mGraphstart;

			doubleClick(event.getX(0), event.getY(0),
					System.currentTimeMillis());

			for (int i = 0; i < mPreviousTime.length; i++) {
				mPreviousTime[i] = System.currentTimeMillis();
			}

			for (int i = 0; i < mPreviousTouches.length; i++) {
				mPreviousTouches[i] = event.getX();

			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			if (pointerCount == 2) {
				mSwype = true;
				mDrag = false;
				mDiffrentX = Math.abs(event.getX(0) - event.getX(1));
				mDiffrentY = Math.abs(event.getY(0) - event.getY(1)) + 3;
				staticX = (event.getX(0) + event.getX(1)) / 2;
				staticDist = (staticX - mGraphstart) / mRealhWidthRate;
			}
			break;

		case MotionEvent.ACTION_UP:
			if (mGraphstart < 100 && mGraphstart > 0)
				mGraphstart = 0;
			else if ((mGraphstart + mGraphWidth) > (mCanvasWidth - 100)
					&& (mGraphstart + mGraphWidth) < mCanvasWidth) {
				mGraphstart = -mGraphWidth + mCanvasWidth;
			}

			mDrag = false;

			double difDistX = mPreviousTouches[0]
					- mPreviousTouches[mPreviousTouches.length - 1];

			if (difDistX > 0)
				mScrollDirection = false;
			else
				mScrollDirection = true;

			difDistX = Math.abs(difDistX);

			long difTime = mPreviousTime[0]
					- mPreviousTime[mPreviousTime.length
							- mPreviousTouches.length];
			if (difTime != 0)
				mSpeed = (float) (difDistX / mPreviousTouches.length);
			mScrollThreadCheck = true;

			break;

		case MotionEvent.ACTION_POINTER_UP:
			if (pointerCount == 2) {
				mRealhHeightRateTemp = mRealhHeightRate;
				mRealhWidthRateTemp = mRealhWidthRate;
				mSwype = false;
				mDrag = false;
			}
			break;

		case MotionEvent.ACTION_MOVE:

			if (mDrag) {
				evX1 = event.getX(0);

				for (int i = 0; i < (mPreviousTouches.length - 1); i++) {
					mPreviousTouches[i + 1] = mPreviousTouches[i];
				}

				mPreviousTouches[0] = event.getX();

				for (int i = 0; i < (mPreviousTime.length - 1); i++) {
					mPreviousTime[i + 1] = mPreviousTime[i];
				}

				mPreviousTime[0] = System.currentTimeMillis();
				mGraphstart = (evX1 - mDragX);

				if (mGraphstart > 100) {
					mGraphstart = 99;
					mDragX = evX1 - mGraphstart;
					break;
				} else if (((mGraphstart + mGraphWidth) < (mCanvasWidth - 100))) {
					mGraphstart = mCanvasWidth - 100 - mGraphWidth + 1;
					break;
				}

			}
			if (mSwype) {

				// try {
				// TimeUnit.MILLISECONDS.sleep(100);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				float realDiffrentX = Math.abs(event.getX(0) - event.getX(1))
						/ (mDiffrentX);
				float realDiffrentY = Math.abs(event.getY(0) - event.getY(1))
						/ (mDiffrentY);
				if (Math.abs(event.getX(0) - event.getX(1))
						/ Math.abs(event.getY(0) - event.getY(1)) > 1)
					mRealhWidthRate = mRealhWidthRateTemp * realDiffrentX;
				else
					mRealhHeightRate = mRealhHeightRateTemp * realDiffrentY;

				mGraphstart = staticX - staticDist * mRealhWidthRate;

				if (mGraphstart > 0)
					mGraphstart = 0;

				break;
			}

			break;
		}
		return true;
	}

	private void doubleClick(float x, float y, long time) {
		boolean timeBool = (time - mPreviousTime1) < mMaxTime;
		boolean distant = (Math.abs(x - mPreviousTouchX1) < mMaxDistant)
				&& (Math.abs(y - mPrevTouchY) < mMaxDistant);

		// float iks = event.getX(0);

		if (timeBool && distant) {

			// float wqer = (x - mGraphstart) / mRealhWidthRate;

			// mGraphstart = event.getX(0) - (event.getX(0) - mGraphstart)
			// / mRealhWidthRate * 10;
			// final String TAGG = "хуйня";
			//
			// Log.v(TAG, " \n");
			// Log.v(TAG, "iks " + iks);
			// Log.v(TAG, "mGraphstart " + mGraphstart);
			// Log.v(TAG, "mRealhWidthRate " + mRealhWidthRate);
			// Log.v(TAG, "wqer " + wqer);

			mRealhHeightRate = mGraphHeightRate;
			mRealhHeightRateTemp = mGraphHeightRate;
			mRealhWidthRate = 10;
			mRealhWidthRateTemp = 10;

			// mGraphstart = x - wqer;
			//
			// Log.v(TAG, " \n");
			// Log.v(TAG, "iks " + iks);
			// Log.v(TAG, "mGraphstart " + mGraphstart);
			// Log.v(TAG, "mRealhWidthRate " + mRealhWidthRate);
			// Log.v(TAG, "wqer " + wqer);

		}

		mPreviousTime1 = time;
		mPreviousTouchX1 = x;
		mPrevTouchY = y;

	}

	public void graphToBegin() {
		mGraphstart = 0;
	}

}
