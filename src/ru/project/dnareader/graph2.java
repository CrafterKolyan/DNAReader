package ru.project.dnareader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class graph2 extends SurfaceView implements SurfaceHolder.Callback {

	static DrawThread drawThread;

	// public static boolean check = false;

	Path path;
	public static Paint p;

	float x = 10;

	public static int mas[] = null;

	float side = 0;

	public static int[] traceA;
	public static int[] traceC;
	public static int[] traceG;
	public static int[] traceT;

	boolean drag = false;
	float dragX = 0;
	float dragY = 0;

	float canvasWidth = 0;
	float canvasHeight = 0;

	public graph2(Context context, AttributeSet attrs) {
		super(context);
		getHolder().addCallback(this);

		path = new Path();

		p = new Paint();
		p.setStrokeWidth(3);
		// if (check)
		p.setStyle(Paint.Style.STROKE);
		p.setColor(Color.BLUE);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawThread = new DrawThread(getHolder());
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

		@Override
		public void run() {
			// running = check;
			Canvas canvas = null;

			while (running) {
				canvas = null;

				try {
					canvas = surfaceHolder.lockCanvas(null);
					if (canvas == null)
						continue;
					canvasWidth = canvas.getWidth();
					canvasHeight = canvas.getHeight();
					path.reset();
					// float canvasH = canvasHeight;
					// float canvasW = 50;
					path.moveTo(0, canvasHeight);
					// float qwe =
					for (int i = 0; i < traceA.length; i++) {
						path.lineTo(2 * i, canvasHeight - 2 * traceA[i]);
					}
					// if (a == null) {
					// p.setColor(Color.WHITE);
					//
					// }
					// while (canvasH > 100) {
					// path.lineTo(x + canvasW, canvasHeight - canvasH);
					// path.lineTo(x + canvasW + 50, canvasHeight);
					// canvasW += 100;
					// canvasH -= 100;
					// }

					// for (int i = 0; i < mas.length; i++) {
					// path.lineTo(mas[i], canvasW);
					// canvasW += 50;
					// }

					// side = canvasW - 50;

					// canvas.drawPoint(50, 50, p);
					canvas.drawColor(Color.WHITE);
					canvas.drawPath(path, p);

					// invalidate();
					// requestLayout();
					// canvas.drawColor(Color.GREEN);
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}

			}
		}
	}

}
