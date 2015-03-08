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
	private LinkedHashMap<String, Integer> commentCount;
	private LinkedHashMap<String, Integer> maxDigit;
	private List<CommitComment> rawComment;

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
		if (rawComment == null)
			rawComment = new ArrayList<CommitComment>();
		if (comments == null)
			return;
		for (int i = comments.size() - 1; i >= 0; i--) {
			CommitComment cc = comments.get(i);
			if (isRaw(cc)) {
				rawComment.add(0, cc);
				continue;
			}
			String fileName = cc.getPath();
			commentCountIncrement(fileName);
			childMap.get(fileName).add(cc.getPosition() + 1, cc);
		}
	}

	public void commentCountIncrement(String fileName) {
		if (commentCount == null)
			commentCount = new LinkedHashMap<String, Integer>();
		if (commentCount.containsKey(fileName)) {
			commentCount.put(fileName, commentCount.get(fileName) + 1);
		} else {
			commentCount.put(fileName, 1);
		}
	}

	public void commentCountDescrement(String fileName) {
		if (commentCount == null)
			commentCount = new LinkedHashMap<String, Integer>();
		if (commentCount.containsKey(fileName)) {
			if (commentCount.get(fileName) == 0)
				return;
			commentCount.put(fileName, commentCount.get(fileName) - 1);
		} else {
			commentCount.put(fileName, 1);
		}
	}

	public void commentCountIncrement(int position) {
		String fileName = commitFiles.get(position).getFilename();
		commentCountIncrement(fileName);
	}

	public void commentCountDescrement(int position) {
		String fileName = commitFiles.get(position).getFilename();
		commentCountDescrement(fileName);
	}

	public int getFileCommentCount(String fileName) {
		if (commentCount == null)
			return 0;
		if (!commentCount.containsKey(fileName)
				|| commentCount.get(fileName) == null)
			return 0;
		return commentCount.get(fileName);
	}

	public int getFileCommentCount(int position) {
		String fileName = commitFiles.get(position).getFilename();
		return getFileCommentCount(fileName);
	}

	public boolean isRaw(CommitComment comment) {
		return TextUtils.isEmpty(comment.getPath()) && comment.getLine() == 0;
	}

	//
	// public void addComment(CommitComment cc) {
	// if (commentMap == null) {
	// commentMap = new LinkedHashMap<String, List<CommitComment>>();
	// }
	// String fileName = cc.getPath();
	// if (!commentMap.containsKey(fileName)) {
	// List<CommitComment> comments = new ArrayList<CommitComment>();
	// commentMap.put(fileName, comments);
	// }
	// commentMap.get(fileName).add(cc);
	//
	// }

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
			int position = 0;
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
						commitLine.setPosition(position++);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// System.out.println(commitLine.getPosition() + ":"+
				// commitLine.getLine());
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

	public List<CommitComment> getRawComment() {
		return rawComment;
	}

	public void setRawComment(List<CommitComment> rawComment) {
		this.rawComment = rawComment;
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

	public List<Serializable> getChildList(int groupPosition) {
		return childMap.get(commitFiles.get(groupPosition).getFilename());
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
		return getGroupCommentCount() + getGroupFileCount();
	}

	public int getGroupFileCount() {
		if (commitFiles != null)
			return commitFiles.size();
		return 0;
	}

	public int getGroupCommentCount() {
		if (rawComment != null)
			return rawComment.size();
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

	public CommitComment getCommitComment(int position) {
		if (rawComment != null)
			return rawComment.get(position);
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
