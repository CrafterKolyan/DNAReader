//package ru.project.dnareader;
//
////*****************************************************************************************
////******** 																		***********
////********       Работающая копия вывода графика используя отдельный поток      ***********
////********																		***********
////*****************************************************************************************
//
//import org.biojava.bio.seq.DNATools;
//import org.biojava.bio.symbol.AtomicSymbol;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.TextView;
//
//public class graphic3 extends SurfaceView implements SurfaceHolder.Callback {
//
//	static DrawThread drawThread;
//
//	private static final String TAG = "DnaReader";
//
//	TextView tv;
//
//	float staticX = 0;
//	float staticDist = 0;
//
//	static float graphstart = 0;
//
//	float graphWidth = 0;
//
//	long prevTime = 9999;
//	long maxTime = 600;
//	float maxDistant = 100;
//	float prevTouchY = 99999;
//	float prevTouchX = 99999;
//
//	static public int[] baseCallsX = null;
//	static public String baseCallsLetters = null;
//
//	static boolean isDrawing = false;
//
//	private char[] temp = null;
//
//	static public Sequence secA = new Sequence(DNATools.a());
//	static public Sequence secC = new Sequence(DNATools.c());
//	static public Sequence secG = new Sequence(DNATools.g());
//	static public Sequence secT = new Sequence(DNATools.t());
//
//	static Canvas canvas = null;
//
//	static boolean checkHeightRate = true;
//
//	boolean drag = false;
//	boolean swype = false;
//
//	float dragX = 0;
//	float dragY = 0;
//
//	static float maxHeigt = 0;
//
//	static float graphHeightRate = 1;
//
//	static float realhHeightRate = 1;
//	static float realhWidthRate = 10;
//
//	static float realhHeightRate2 = 1;
//	static float realhWidthRate2 = 10;
//
//	float diffrentX = 0;
//	float diffrentY = 0;
//
//	float canvasWidth = 0;
//	float canvasHeight = 0;
//
//	public graphic3(Context context, AttributeSet attrs) {
//		super(context);
//		Log.v(TAG, "Graphic Graphic");
//		getHolder().addCallback(this);
//
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width,
//			int height) {
//		checkHeightRate = true;
//		Log.v(TAG, "Graphic surfaceChanged");
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		drawThread = new DrawThread(getHolder());
//		Log.v(TAG, "Graphic surfaceCreated");
//		drawThread.start();
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.v(TAG, "Graphic surfaceDestroyed");
//		drawThread.interrupt();
//		try {
//			drawThread.join();
//		} catch (InterruptedException e) {
//			Log.e(TAG, "Exception:", e);
//		}
//	}
//
//	class DrawThread extends Thread {
//
//		private SurfaceHolder surfaceHolder;
//
//		public DrawThread(SurfaceHolder surfaceHolder) {
//			this.surfaceHolder = surfaceHolder;
//			Log.v(TAG, "Graphic DrawThread");
//		}
//
//		private void drawingGraph(int[] trace, AtomicSymbol base) {
//			// Log.v(TAG, "Graphic drawingGraph");
//			Path path = new Path();
//			path.reset();
//			path.moveTo(graphstart, canvasHeight - trace[0] * realhHeightRate);
//
//			int beggining = 0;
//			int ending = 0;
//
//			beggining = Math.abs((int) (graphstart / realhWidthRate));
//			ending = beggining + (int) (canvasWidth / realhWidthRate) + 3;
//
//			if (ending > trace.length)
//				ending = trace.length;
//
//			for (int i = beggining; i < ending; i += 1) {
//				if ((canvasHeight - trace[i] * realhHeightRate) < 50)
//					path.lineTo(graphstart + i * realhWidthRate, 50);
//				else
//					path.lineTo(graphstart + i * realhWidthRate, canvasHeight
//							- trace[i] * realhHeightRate);
//			}
//
//			if (base == DNATools.a()) {
//				secA.path = path;
//			} else if (base == DNATools.c()) {
//				secC.path = path;
//			} else if (base == DNATools.g()) {
//				secG.path = path;
//			} else if (base == DNATools.t()) {
//				secT.path = path;
//			}
//
//		}
//
//		void drawSymbols(Canvas canvas) {
//			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//			paint.setTextSize(50);
//			paint.setStyle(Paint.Style.FILL_AND_STROKE);
//			paint.setColor(Color.YELLOW);
//
//			temp = baseCallsLetters.toCharArray();
//			for (int i = 0; i < temp.length; i++) {
//				temp[i] = Character.toUpperCase(temp[i]);
//
//			}
//
//			for (int i = 0; i < temp.length; i++) {
//				switch (temp[i]) {
//				case 'A':
//					paint.setColor(Color.GREEN);
//					break;
//				case 'C':
//					paint.setColor(Color.BLUE);
//					break;
//				case 'G':
//					paint.setColor(Color.BLACK);
//					break;
//				case 'T':
//					paint.setColor(Color.RED);
//					break;
//				}
//
//				canvas.drawText(temp, i, 1, graphstart
//						+ (float) (baseCallsX[i]) * realhWidthRate - 17, 40,
//						paint);
//			}
//
//		}
//
//		@Override
//		public void run() {
//
//			Log.v(TAG, "Graphic run");
//
//			while (!drawThread.isInterrupted()) {
//
//				canvas = null;
//
//				try {
//					canvas = surfaceHolder.lockCanvas(null);
//
//					if (canvas == null || graphHeightRate == 0)
//						return;
//
//					canvas.drawColor(Color.WHITE);
//
//					if (isDrawing) {
//						if (secA == null)
//							Log.v(TAG, "secA == null");
//
//						// Log.v(TAG, "graphWidth " + graphWidth);
//						// Log.v(TAG, "secA.trace.length " + secA.trace.length);
//						// Log.v(TAG, "realhWidthRate " + realhWidthRate);
//
//						graphWidth = secA.trace.length * realhWidthRate;
//						if (checkHeightRate) {
//							maxHeigt = 0;
//							canvasWidth = canvas.getWidth();
//							canvasHeight = canvas.getHeight() - 10;
//							maxHeigt = Math.max(Math.max(secA.max, secC.max),
//									Math.max(secG.max, secT.max));
//							graphHeightRate = (canvasHeight - 40) / maxHeigt;
//							checkHeightRate = false;
//							realhHeightRate = graphHeightRate;
//							realhHeightRate2 = graphHeightRate;
//						}
//
//						drawingGraph(secA.trace, DNATools.a());
//						drawingGraph(secC.trace, DNATools.c());
//						drawingGraph(secG.trace, DNATools.g());
//						drawingGraph(secT.trace, DNATools.t());
//
//						canvas.drawPath(secA.path, secA.paint);
//						canvas.drawPath(secC.path, secC.paint);
//						canvas.drawPath(secG.path, secG.paint);
//						canvas.drawPath(secT.path, secT.paint);
//
//						drawSymbols(canvas);
//						// return;
//					}
//				} finally {
//					if (canvas != null) {
//						surfaceHolder.unlockCanvasAndPost(canvas);
//					}
//				}
//
//			}
//
//		}
//
//	}
//
//	@SuppressLint("ClickableViewAccessibility")
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		float evX1 = 0;
//		int actionMask = event.getActionMasked();
//		int pointerCount = event.getPointerCount();
//
//		switch (actionMask) {
//		case MotionEvent.ACTION_DOWN:
//			drag = true;
//			evX1 = event.getX(0);
//			dragX = evX1 - graphstart;
//			doubleClick(event);
//			break;
//
//		case MotionEvent.ACTION_POINTER_DOWN:
//			if (pointerCount == 2) {
//				swype = true;
//				drag = false;
//				diffrentX = Math.abs(event.getX(0) - event.getX(1));
//				diffrentY = Math.abs(event.getY(0) - event.getY(1)) + 3;
//				staticX = (event.getX(0) + event.getX(1)) / 2;
//				staticDist = (Math.abs((graphstart - staticX) / realhWidthRate));
//			}
//			break;
//
//		case MotionEvent.ACTION_UP:
//			if (graphstart < 100 && graphstart > 0)
//				graphstart = 0;
//			else if ((graphstart + graphWidth) > 980
//					&& (graphstart + graphWidth) < canvasWidth)
//				graphstart = -graphWidth + canvasWidth;
//			drag = false;
//			break;
//
//		case MotionEvent.ACTION_POINTER_UP:
//			if (pointerCount == 2) {
//				realhHeightRate2 = realhHeightRate;
//				realhWidthRate2 = realhWidthRate;
//				swype = false;
//				drag = false;
//			}
//			break;
//
//		case MotionEvent.ACTION_MOVE:
//			MainActivity.tv1.setText(" " + graphWidth);
//			MainActivity.tv2.setText(" " + canvasWidth);
//
//			if (drag) {
//				evX1 = event.getX(0);
//				graphstart = (evX1 - dragX);
//				if (graphstart > 100) {
//					graphstart = 99;
//					dragX = evX1 - graphstart;
//					break;
//				} else if (((graphstart + graphWidth) < (canvasWidth - 100))) {
//					graphstart = canvasWidth - 100 - graphWidth + 1;
//
//					break;
//				}
//				// inv();
//			}
//			if (swype) {
//
//				// try {
//				// TimeUnit.MILLISECONDS.sleep(100);
//				// } catch (InterruptedException e) {
//				// e.printStackTrace();
//				// }
//
//				float realDiffrentX = Math.abs(event.getX(0) - event.getX(1))
//						/ (diffrentX);
//				float realDiffrentY = Math.abs(event.getY(0) - event.getY(1))
//						/ (diffrentY);
//				if (Math.abs(event.getX(0) - event.getX(1))
//						/ Math.abs(event.getY(0) - event.getY(1)) > 1)
//					realhWidthRate = realhWidthRate2 * realDiffrentX;
//				else
//					realhHeightRate = realhHeightRate2 * realDiffrentY;
//
//				graphstart = staticX - staticDist * realhWidthRate;
//
//				break;
//			}
//
//			break;
//		}
//		return true;
//	}
//
//	private void doubleClick(MotionEvent event) {
//		boolean time = (System.currentTimeMillis() - prevTime) < maxTime;
//		boolean distant = ((event.getX() - prevTouchX) < maxDistant)
//				&& ((event.getY() - prevTouchY) < maxDistant);
//
//		if (time && distant) {
//			realhHeightRate = graphHeightRate;
//			realhHeightRate2 = graphHeightRate;
//			realhWidthRate = 10;
//			realhWidthRate2 = 10;
//		}
//
//		prevTime = System.currentTimeMillis();
//		prevTouchX = event.getX();
//		prevTouchY = event.getY();
//
//	}
//
//	// private void inv() {
//	//
//	// drawThread.start();
//	//
//	// }
//
// }
