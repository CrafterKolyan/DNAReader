package ru.project.dnareader;

import java.io.File;
import java.io.IOException;
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
	private float[][] mPreviousTouches = new float[10][2];
	private long[] mPreviousTime = new long[10];
	private long mPreviousTime1 = Long.MAX_VALUE;
	private float mPreviousTouchX1 = Float.MAX_VALUE;
	private float mPrevTouchY = Float.MAX_VALUE;
	private long mMaxTime = 500;
	private float mMaxDistant = 50;
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

	private Context mContext = null;

	@SuppressWarnings("unused")
	private float mDragY = 0;

	public Graphic(Context context) {
		super(context);

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
		Log.v(TAG, "Graphic Graphic 3");
		getHolder().addCallback(this);
	}

	public void newData(File file, Context context) {
		mCheckHeightRate = true;

		mContext = context;

		try {

			ABITrace abiTrace = new ABITrace(file);
			mSecA.trace(abiTrace.getTrace(DNATools.a()));
			mSecC.trace(abiTrace.getTrace(DNATools.c()));
			mSecG.trace(abiTrace.getTrace(DNATools.g()));
			mSecT.trace(abiTrace.getTrace(DNATools.t()));

			mBaseCallsX = abiTrace.getBasecalls();
			mBaseCallsLetters = abiTrace.getSequence().seqString()
					.toCharArray();
			mIsDrawing = true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalSymbolException e) {
			e.printStackTrace();
		}
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

		// char[] qwer = { 'х', 'у', 'й', 'н', 'я' };

		// if (tv1 == null) {
		// // Toast.makeText(mContext, "tv1 == null",
		// // Toast.LENGTH_LONG).show();
		// Log.v("TAGg", "х");
		// for (int i = 0; i < 100; i++)
		// Log.v("TAGg", "у");
		// Log.v("TAGg", "й");
		// }

		mDrawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(TAG, "Graphic surfaceDestroyed");
		mDrawThread.interrupt();
		try {
			mDrawThread.join();
		} catch (InterruptedException e) {
			Log.e(TAG, "Exception:", e);
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

			int beggining = 0;
			int ending = 0;

			beggining = Math.abs((int) (mGraphstart / mRealhWidthRate));
			ending = beggining + (int) (mCanvasWidth / mRealhWidthRate) + 3;

			MainActivity.tv2.setText("beggining: " + beggining);
			MainActivity.tv3.setText("mGraphHeightRate: " + mGraphHeightRate);

			if (ending > trace.length)
				ending = trace.length;

			for (int i = beggining; i < ending; i += 1) {
				if ((mCanvasHeight - trace[i] * mRealhHeightRate) < 50)
					path.lineTo(mGraphstart + i * mRealhWidthRate, 50);
				else
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

		@SuppressWarnings("unused")
		private void drawSymbols(Canvas canvas) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextSize(50);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setColor(Color.YELLOW);

			for (int i = 0; i < mBaseCallsLetters.length; i++) {
				mBaseCallsLetters[i] = Character
						.toUpperCase(mBaseCallsLetters[i]);

			}

			for (int i = 0; i < mBaseCallsLetters.length; i++) {
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

				canvas.drawText(mBaseCallsLetters, i, 1, mGraphstart
						+ (float) (mBaseCallsX[i]) * mRealhWidthRate, 40, paint);
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

						mGraphWidth = mSecA.trace.length * mRealhWidthRate;
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

						drawSymbols(mCanvas);

						drawingGraph(mSecA.trace, DNATools.a());
						drawingGraph(mSecC.trace, DNATools.c());
						drawingGraph(mSecG.trace, DNATools.g());
						drawingGraph(mSecT.trace, DNATools.t());

						mCanvas.drawPath(mSecA.path, mSecA.paint);
						mCanvas.drawPath(mSecC.path, mSecC.paint);
						mCanvas.drawPath(mSecG.path, mSecG.paint);
						mCanvas.drawPath(mSecT.path, mSecT.paint);

						MainActivity.tv1.setText("mGraphtart: " + mGraphstart);
						// tv2.
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
					mSpeed -= 0.01;

					if (mScrollDirection) {
						mGraphstart -= mSpeed;
					} else {
						mGraphstart += mSpeed;
					}

					if (mGraphstart > 0) {
						mScrollThreadCheck = false;
						break;
					}

					try {
						TimeUnit.MILLISECONDS.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
			doubleClick(event);
			for (int i = 0; i < mPreviousTime.length; i++) {
				mPreviousTime[i] = System.currentTimeMillis();
			}

			for (int i = 0; i < mPreviousTouches.length; i++) {
				mPreviousTouches[i][0] = event.getX();
				mPreviousTouches[i][1] = event.getY();

			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			if (pointerCount == 2) {
				mSwype = true;
				mDrag = false;
				mDiffrentX = Math.abs(event.getX(0) - event.getX(1));
				mDiffrentY = Math.abs(event.getY(0) - event.getY(1)) + 3;
				staticX = (event.getX(0) + event.getX(1)) / 2;
				staticDist = (Math.abs((mGraphstart - staticX)
						/ mRealhWidthRate));
			}
			break;

		case MotionEvent.ACTION_UP:
			if (mGraphstart < 100 && mGraphstart > 0)
				mGraphstart = 0;
			else if ((mGraphstart + mGraphWidth) > 980
					&& (mGraphstart + mGraphWidth) < mCanvasWidth)
				mGraphstart = -mGraphWidth + mCanvasWidth;

			mDrag = false;

			double difDistX = mPreviousTouches[0][0]
					- mPreviousTouches[mPreviousTouches.length - 1][0];

			if (difDistX > 0)
				mScrollDirection = false;
			else
				mScrollDirection = true;

			difDistX = Math.abs(difDistX);

			long difTime = mPreviousTime[0]
					- mPreviousTime[mPreviousTime.length - 1];

			mSpeed = (float) (difDistX / difTime);

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
			// MainActivity.tv1.setText(" " + mGraphWidth);
			// MainActivity.tv2.setText(" " + mCanvasWidth);

			if (mDrag) {
				evX1 = event.getX(0);

				for (int i = 0; i < (mPreviousTouches.length - 1); i++) {
					mPreviousTouches[i + 1][0] = mPreviousTouches[i][0];
					mPreviousTouches[i + 1][1] = mPreviousTouches[i][1];
				}

				mPreviousTouches[0][0] = event.getX();
				mPreviousTouches[0][1] = event.getY();

				for (int i = 0; i < (mPreviousTime.length - 1); i++) {
					mPreviousTime[i + 1] = mPreviousTime[i];
				}

				mPreviousTime[0] = System.currentTimeMillis();

				mGraphstart = (evX1 - mDragX);

				if (mGraphstart > 100) {
					mGraphstart = 99;
					mDragX = evX1 - mGraphstart;
					invalidate();
					break;
				} else if (((mGraphstart + mGraphWidth) < (mCanvasWidth - 100))) {
					mGraphstart = mCanvasWidth - 100 - mGraphWidth + 1;
					invalidate();
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

				break;
			}

			break;
		}
		return true;
	}

	private void doubleClick(MotionEvent event) {
		boolean time = (System.currentTimeMillis() - mPreviousTime1) < mMaxTime;
		boolean distant = ((event.getX() - mPreviousTouchX1) < mMaxDistant)
				&& ((event.getY() - mPrevTouchY) < mMaxDistant);

		if (time && distant) {
			mRealhHeightRate = mGraphHeightRate;
			mRealhHeightRateTemp = mGraphHeightRate;
			mRealhWidthRate = 10;
			mRealhWidthRateTemp = 10;
		}

		mPreviousTime1 = System.currentTimeMillis();
		mPreviousTouchX1 = event.getX();
		mPrevTouchY = event.getY();

	}

	public void graphToBegin() {
		mGraphstart = 0;
	}

}
