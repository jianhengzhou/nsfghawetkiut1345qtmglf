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
	private LinkedHashMap<String, List<Serializable>> childMap;
	private LinkedHashMap<String, List<CommitComment>> commentMap;
	private LinkedHashMap<String, Integer> maxDigit;

	public CommitTree(RepositoryCommit commit, List<CommitComment> comments) {
		commitFiles = commit.getFiles();
		initLines();
		try {
			addComment(comments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for (CommitComment cc : comments) {
		// addComment(cc);
		// }
	}

	public void addComment(List<CommitComment> comments) {
		for (int i = comments.size() - 1; i >= 0; i--) {
			CommitComment cc = comments.get(i);
			String fileName = cc.getPath();
			childMap.get(fileName).add(cc.getPosition() + 1, cc);
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
		childMap = new LinkedHashMap<String, List<Serializable>>();

		maxDigit = new LinkedHashMap<String, Integer>(commitFiles.size());
		for (CommitFile cf : commitFiles) {
			String patch = cf.getPatch();
			if (TextUtils.isEmpty(patch))
				continue;

			List<Serializable> lines = new ArrayList<Serializable>();
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
			childMap.put(cf.getFilename(), lines);

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

	public LinkedHashMap<String, List<Serializable>> getChildMap() {
		return childMap;
	}

	public void setChildMap(LinkedHashMap<String, List<Serializable>> childMap) {
		this.childMap = childMap;
	}

	public Serializable getChild(int groupPosition, int childPosition) {
		return childMap.get(commitFiles.get(groupPosition).getFilename()).get(
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
			return childMap.get(commitFiles.get(groupPosition).getFilename())
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
