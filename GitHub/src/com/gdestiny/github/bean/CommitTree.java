package com.gdestiny.github.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;

import android.text.TextUtils;

import com.gdestiny.github.utils.CommonUtils;

public class CommitTree implements Serializable {

	private static final long serialVersionUID = 2384936695740654366L;

	private List<CommitFile> commitFiles;
	private LinkedHashMap<String, List<String>> linesMap;

	public CommitTree(RepositoryCommit commit) {
		commitFiles = commit.getFiles();
		initLines();
	}

	private void initLines() {
		linesMap = new LinkedHashMap<String, List<String>>(commitFiles.size());
		for (CommitFile cf : commitFiles) {
			String patch = cf.getPatch();
			if (TextUtils.isEmpty(patch))
				continue;

			List<String> lines = new ArrayList<String>();
			int length = patch.length();
			int start = 0;
			int end = patch.indexOf("\n", start);
			while (start < length) {
				lines.add(patch.substring(start, end));
				System.out.println(patch.substring(start, end));
				start = end + 1;
				end = patch.indexOf("\n", start);
				if(end < 0)
					end = length;
			}

			linesMap.put(cf.getFilename(), lines);
		}
	}

	public LinkedHashMap<String, List<String>> getLinesMap() {
		return linesMap;
	}

	public void setLinesMap(LinkedHashMap<String, List<String>> linesMap) {
		this.linesMap = linesMap;
	}

	public String getLines(int groupPosition, int childPosition) {
		return linesMap.get(commitFiles.get(groupPosition).getFilename()).get(
				childPosition);
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
