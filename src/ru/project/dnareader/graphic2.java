//package ru.project.dnareader;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.view.MotionEvent;
//import android.view.View;
//
//public class graphic2 extends View {
//
//	Path path;
//	public static Paint p;
//
//	float x = 10;
//
//	float side = 0;
//
//	int mas[] = null;
//
//	public static int[] traceA;
//	public static int[] traceC;
//	public static int[] traceG;
//	public static int[] traceT;
//
//	boolean drag = false;
//	float dragX = 0;
//	float dragY = 0;
//
//	float canvasWidth = 0;
//	float canvasHeight = 0;
//
//	public static int[] a;
//
//	graphic2(Context context, int[] aa, int[] c, int[] g, int[] t) {
//		super(context);
//
//		path = new Path();
//
//		p = new Paint();
//		p.setStrokeWidth(6);
//		p.setStyle(Paint.Style.STROKE);
//		p.setColor(Color.BLUE);
//
//	}
//
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		canvasWidth = canvas.getWidth();
//		canvasHeight = canvas.getHeight();
//		path.reset();
//		float canvasH = canvasHeight;
//		float canvasW = 50;
//		path.moveTo(x, canvasHeight);
//		for (int i = 0; i < mas.length; i++) {
//			path.lineTo(mas[i], canvasW);
//			canvasW += 50;
//		}
//		side = canvasW - 50;
//
//		canvas.drawPath(path, p);
//		invalidate();
//	}
//
//	@SuppressLint("ClickableViewAccessibility")
//	public boolean onTouchEvent(MotionEvent event) {
//		float evX = event.getX();
//
//		switch (event.getAction()) {
//
//		case MotionEvent.ACTION_DOWN:
//			drag = true;
//			dragX = evX - x;
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if (drag) {
//				x = evX - dragX;
//				if (x > 100) {
//					x = 99;
//					dragX = evX - x;
//					invalidate();
//					break;
//				} else if ((x + side) < (canvasWidth - 100)) {
//					x = canvasWidth - 100 - side + 1;
//					invalidate();
//					break;
//				}
//				invalidate();
//			}
//
//			break;
//
//		case MotionEvent.ACTION_UP:
//			if (x < 100 && x > 0)
//				x = 0;
//			else if ((x + side) > 980 && (x + side) < canvasWidth)
//				x = -side + canvasWidth;
//			drag = false;
//			break;
//		}
//
//		return true;
//	}
// }
