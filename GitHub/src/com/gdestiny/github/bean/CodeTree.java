package com.gdestiny.github.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;

import com.gdestiny.github.bean.comparator.TreeEntryComparator;
import com.gdestiny.github.utils.CommonUtils;
import com.gdestiny.github.utils.GLog;

public class CodeTree implements Serializable, Comparable<CodeTree> {

	private static final long serialVersionUID = 1L;

	public static final String ROOT = "root system";

	public static HashMap<String, CodeTree> allFolder = new HashMap<String, CodeTree>();

	public String parent;
	public String name;

	public TreeEntry currEntry;
	public List<CodeTree> subTree;
	public List<TreeEntry> subEntry;

	public CodeTree() {
		this.currEntry = null;
		this.name = ROOT;
		this.parent = "";
		subTree = new ArrayList<CodeTree>();
		subEntry = new ArrayList<TreeEntry>();
	}

	public CodeTree(TreeEntry currEntry) {
		this.currEntry = currEntry;
		this.name = CommonUtils.pathToName(currEntry.getPath());
		this.parent = CommonUtils.pathToParentName(currEntry.getPath());
		subTree = new ArrayList<CodeTree>();
		subEntry = new ArrayList<TreeEntry>();
	}

	public static CodeTree toCodeTree(Tree tree) {
		if (tree == null) {
			GLog.sysout("tree is null");
			return null;
		}
		allFolder.clear();
		allFolder.put(ROOT, new CodeTree());

		List<TreeEntry> teList = tree.getTree();
		for (TreeEntry currEntry : teList) {
			String type = currEntry.getType();
			String path = currEntry.getPath();
			String parent = CommonUtils.pathToParentName(path);
			if (TreeEntry.TYPE_TREE.equals(type)) {
				CodeTree ct = new CodeTree(currEntry);
				allFolder.put(path, ct);
				allFolder.get(parent).subTree.add(ct);
			} else if (TreeEntry.TYPE_BLOB.equals(type)) {
				allFolder.get(parent).subEntry.add(currEntry);
			}
		}
		// sort
		long temp = System.currentTimeMillis();
		TreeEntryComparator tc = new TreeEntryComparator();
		Iterator<Entry<String, CodeTree>> iter = allFolder.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, CodeTree> entry = (Map.Entry<String, CodeTree>) iter
					.next();
			CodeTree codeTree = entry.getValue();
			Collections.sort(codeTree.subTree);
			Collections.sort(codeTree.subEntry, tc);
		}
		GLog.sysout("sort allFolder:" + (System.currentTimeMillis() - temp));
		return allFolder.get(ROOT);
	}

	@Override
	public int compareTo(CodeTree o) {
		return this.name.compareToIgnoreCase(o.name);
	}

	public int getCount() {
		return subEntry.size() + subTree.size();
	}

	public int getTreeCount() {
		return subTree.size();
	}

	public int getEntryCount() {
		return subEntry.size();
	}
}
