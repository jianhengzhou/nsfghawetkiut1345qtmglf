package com.gdestiny.github.bean;

import java.io.Serializable;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class CommitLine implements Serializable {

	private static final long serialVersionUID = -3741699368891166965L;

	private int oldLine;
	private int newLine;
	private int position;
	private String line;

	public boolean isAddition() {
		return line.startsWith("+");
	}

	public boolean isDeletion() {
		return line.startsWith("-");
	}

	public boolean isMark() {
		return line.startsWith("@") || line.startsWith("\\");
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public CommitLine setOldLine(int oldLine) {
		this.oldLine = oldLine;
		return this;
	}

	public SpannableString getOldLine(int maxlength) {
		return format(maxlength, oldLine);
	}

	public SpannableString getNewLine(int maxlength) {
		return format(maxlength, newLine);
	}

	public int getOldLine() {
		return oldLine;
	}

	public int getNewLine() {
		return newLine;
	}

	private SpannableString format(int maxlength, int num) {
		SpannableString span;
		String numStr;
		if (num == -1)
			numStr = "..";
		else if (num == 0)
			numStr = "";
		else
			numStr = "" + num;

		int delta = 0;
		if (num == -1)
			delta = maxlength - 1;
		else
			delta = maxlength - numStr.length();
		String temp = "";
		for (int i = 0; i < delta; i++) {
			temp += "0";
		}
		span = new SpannableString(temp + numStr);
		span.setSpan(new ForegroundColorSpan(0x00000000), 0, temp.length(),
				SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

		return span;
	}

	public CommitLine setNewLine(int newLine) {
		this.newLine = newLine;
		return this;
	}

	public String getLine() {
		return line;
	}

	public CommitLine setLine(String line) {
		this.line = line;
		return this;
	}
}
