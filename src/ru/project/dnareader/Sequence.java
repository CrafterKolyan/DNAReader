package ru.project.dnareader;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.AtomicSymbol;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Sequence {
	public int[] trace = null;
	public int color = 0;
	public Paint paint = new Paint();
	public int max = 0;
	public Path path;

	public Sequence(AtomicSymbol base) {

		if (base == DNATools.a()) {
			color = Color.GREEN;
			paint.setColor(Color.GREEN);
		} else if (base == DNATools.c()) {
			color = Color.BLUE;
			paint.setColor(Color.BLUE);
		} else if (base == DNATools.g()) {
			color = Color.BLACK;
			paint.setColor(Color.BLACK);
		} else if (base == DNATools.t()) {
			color = Color.RED;
			paint.setColor(Color.RED);
		}

		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);

	}

	public void trace(int[] trace) {
		this.trace = trace;
		max();
	}

	private void max() {
		for (int i = 0; i < trace.length; i++)
			if (max < trace[i])
				max = trace[i];
	}

}
