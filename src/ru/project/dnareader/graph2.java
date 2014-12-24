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

	private DrawThread drawThread;

	Path path;
	public static Paint p;

	float x = 10;

	float side = 0;

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
		p.setStrokeWidth(6);
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
		drawThread.setRunning(true);
		drawThread.start();
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
			Canvas canvas;
			while (running) {
				canvas = null;
				try {
					canvas = surfaceHolder.lockCanvas(null);
					if (canvas == null)
						continue;
					canvasWidth = canvas.getWidth();
					canvasHeight = canvas.getHeight();
					path.reset();
					float canvasH = canvasHeight;
					float canvasW = 50;
					path.moveTo(x, canvasHeight);
					// if (a == null) {
					// p.setColor(Color.WHITE);
					//
					// }
					while (canvasH > 100) {
						path.lineTo(x + canvasW, canvasHeight - canvasH);
						path.lineTo(x + canvasW + 50, canvasHeight);
						canvasW += 100;
						canvasH -= 100;
					}
					side = canvasW - 50;

					canvas.drawPath(path, p);
					// invalidate();
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
