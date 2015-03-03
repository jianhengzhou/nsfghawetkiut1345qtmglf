package com.gdestiny.github.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.text.TextUtils;

import com.gdestiny.github.utils.CommonUtils;

public class CommitTree implements Serializable {

	private static final long serialVersionUID = 2384936695740654366L;

	private List<CommitFile> commitFiles;
	private LinkedHashMap<String, List<CommitLine>> linesMap;
	private LinkedHashMap<String, List<CommitComment>> commentMap;
	private LinkedHashMap<String, Integer> maxDigit;

	public CommitTree(RepositoryCommit commit, List<CommitComment> comments) {
		commitFiles = commit.getFiles();
		initLines();
		for (CommitComment cc : comments) {
			addComment(cc);
		}
	}

	public void addComment(CommitComment cc) {
		if (commentMap == null) {
			commentMap = new LinkedHashMap<String, List<CommitComment>>();
		}
		String fileName = cc.getPath();
		if (!commentMap.containsKey(fileName)) {
			List<CommitComment> comments = new ArrayList<CommitComment>();
			commentMap.put(fileName, comments);
		}
		commentMap.get(fileName).add(cc);

	}

	private void initLines() {
		linesMap = new LinkedHashMap<String, List<CommitLine>>(
				commitFiles.size());
		maxDigit = new LinkedHashMap<String, Integer>(commitFiles.size());
		for (CommitFile cf : commitFiles) {
			String patch = cf.getPatch();
			if (TextUtils.isEmpty(patch))
				continue;
			System.out.println("status:"+cf.getStatus());

			List<CommitLine> lines = new ArrayList<CommitLine>();
			int length = patch.length();
			int start = 0;
			int end = patch.indexOf("\n", start);
			if (end < 0)
				end = length;
			int oldLine = 0;
			int newLine = 0;
			while (start < length) {
				CommitLine commitLine = new CommitLine();
				String line = patch.substring(start, end);
				commitLine.setLine(line);
				// ´¦Àítag
				try {
					if (line.startsWith("@")) {
						oldLine = getOld(line);
						newLine = getNew(line);
						commitLine.setNewLine(-1).setOldLine(-1);
					} else if (line.startsWith("\\")) {
						commitLine.setNewLine(-1).setOldLine(-1);
					} else {
						if (!line.startsWith("-")) {
							commitLine.setNewLine(newLine);
							newLine++;
						}
						if (!line.startsWith("+")) {
							commitLine.setOldLine(oldLine);
							oldLine++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				lines.add(commitLine);
				start = end + 1;
				end = patch.indexOf("\n", start);
				if (end < 0)
					end = length;
			}
			maxDigit.put(cf.getFilename(),
					String.valueOf(Math.max(oldLine, newLine)).length());
			linesMap.put(cf.getFilename(), lines);
		}
	}

	private int getOld(String line) {
		int start = line.indexOf("-") + 1;
		int end = line.indexOf(",", start);
		return Integer.parseInt(line.substring(start, end));
	}

	private int getNew(String line) {
		int start = line.indexOf("+") + 1;
		int end = line.indexOf(",", start);
		return Integer.parseInt(line.substring(start, end));
	}

	public LinkedHashMap<String, List<CommitLine>> getLinesMap() {
		return linesMap;
	}

	public void setLinesMap(LinkedHashMap<String, List<CommitLine>> linesMap) {
		this.linesMap = linesMap;
	}

	public CommitLine getLines(int groupPosition, int childPosition) {
		return linesMap.get(commitFiles.get(groupPosition).getFilename()).get(
				childPosition);
	}

	public int getMaxDigit(int groupPosition) {
		return maxDigit.get(commitFiles.get(groupPosition).getFilename());
	}

	public List<CommitFile> getCommitFiles() {
		return commitFiles;
	}

	public void setCommitFiles(List<CommitFile> commitFiles) {
		this.commitFiles = commitFiles;
	}

	public int getGroupCount() {
		if (commitFiles != null)
			return commitFiles.size();
		return 0;
	}

	public int getChildCount(int groupPosition) {
		try {
			return linesMap.get(commitFiles.get(groupPosition).getFilename())
					.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public CommitFile getCommitFile(int position) {
		if (commitFiles != null)
			return commitFiles.get(position);
		return null;
	}

	public String getFileName(int position) {
		if (commitFiles != null)
			return CommonUtils.pathToName(commitFiles.get(position)
					.getFilename());
		return "";
	}

	public String getFilePath(int position) {
		return CommonUtils.getPath(getFileName(position));
	}
}
