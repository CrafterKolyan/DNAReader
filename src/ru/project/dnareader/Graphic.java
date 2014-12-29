package ru.project.dnareader;

import java.util.concurrent.TimeUnit;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.AtomicSymbol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class Graphic extends SurfaceView implements SurfaceHolder.Callback {

	static DrawThread drawThread;

	// public static boolean check = false;

	TextView tv;

	// Path path1;
	// public static Paint paintTraceA;
	// public static Paint paintTraceC;
	// public static Paint paintTraceG;
	// public static Paint paintTraceT;
	// Path path2;
	// public static Paint p2;

	float x = 10;

	// public static int mas[] = null;

	float side = 0;

	// public static int[] traceA = null;
	// public static int[] traceC = null;
	// public static int[] traceG = null;
	// public static int[] traceT = null;
	//
	// public static int[] avTraceA = null;
	// public static int[] avTraceC = null;
	// public static int[] avTraceG = null;
	// public static int[] avTraceT = null;

	static public Sequence secA = new Sequence(DNATools.a());
	static public Sequence secC = new Sequence(DNATools.c());
	static public Sequence secG = new Sequence(DNATools.g());
	static public Sequence secT = new Sequence(DNATools.t());

	static Canvas canvas = null;

	boolean drag = false;
	float dragX = 0;
	float dragY = 0;

	static float maxHeigt = 0;
	static float heightRate = 0;

	float canvasWidth = 0;
	float canvasHeight = 0;

	float graphicWidth = 0;

	public Graphic(Context context, AttributeSet attrs) {
		super(context);
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawThread = new DrawThread(getHolder());

		Graphic.secA = new Sequence(DNATools.a());
		Graphic.secC = new Sequence(DNATools.c());
		Graphic.secG = new Sequence(DNATools.g());
		Graphic.secT = new Sequence(DNATools.t());

		// drawThread.setRunning(false);
		// drawThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		drawThread.setRunning(false);
		while (retry) {
			try {
				drawThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	class DrawThread extends Thread {

		private boolean running = false;
		private SurfaceHolder surfaceHolder;

		public DrawThread(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		private void drawingGraph(int[] trace, AtomicSymbol base) {

			Path path = new Path();
			path.reset();
			path.moveTo(x, canvasHeight - trace[0] * heightRate);

			for (int i = 1; i < trace.length; i += 5) {
				path.lineTo(x + i * 4, canvasHeight - trace[i] * heightRate);
				// Log.v("TAG", " " + i);
			}
			if (base == DNATools.a()) {
				secA.path = path;
			} else if (base == DNATools.c()) {
				secC.path = path;
			} else if (base == DNATools.g()) {
				secG.path = path;
			} else if (base == DNATools.t()) {
				secT.path = path;
			}

		}

		@Override
		public void run() {
			// running = check;
			// Canvas canvas = null;
			maxHeigt = Math.max(Math.max(secA.max, secC.max),
					Math.max(secG.max, secT.max));

			side = secA.trace.length;

			while (running) {
				canvas = null;

				try {
					canvas = surfaceHolder.lockCanvas(null);

					graphicWidth = secA.trace.length / 3;

					heightRate = (float) (canvas.getHeight()) / maxHeigt;
					heightRate++;

					if (canvas == null || heightRate == 0)
						continue;

					canvas.drawColor(Color.WHITE);

					canvasWidth = canvas.getWidth();
					canvasHeight = canvas.getHeight() - 150;

					drawingGraph(secA.avtrace, DNATools.a());
					drawingGraph(secC.avtrace, DNATools.c());
					drawingGraph(secG.avtrace, DNATools.g());
					drawingGraph(secT.avtrace, DNATools.t());

					canvas.drawPath(secA.path, secA.paint);
					canvas.drawPath(secC.path, secC.paint);
					canvas.drawPath(secG.path, secG.paint);
					canvas.drawPath(secT.path, secT.paint);
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}

			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		float evX = event.getX();

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			drag = true;
			dragX = evX - x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (drag) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x = (evX - dragX);
				MainActivity.tv1.setText(" " + x);
				if (x > 100) {
					x = 99;
					dragX = evX - x;
					invalidate();
					break;
				} else if ((x + side) < (canvasWidth - 100)) {
					x = canvasWidth - 100 - side + 1;
					invalidate();
					break;
				}

			}
			break;

		case MotionEvent.ACTION_UP:
			// if (x < 100 && x > 0)
			// x = 0;
			// else if ((x + side) > 980 && (x + side) < canvasWidth)
			// x = -side + canvasWidth;
			// drag = false;
			break;
		}

		return true;
	}
}
