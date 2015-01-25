package com.gdestiny.github.bean.comparator;

import java.util.Comparator;

import org.eclipse.egit.github.core.TreeEntry;

import com.gdestiny.github.utils.CommonUtils;

public class TreeEntryComparator implements Comparator<TreeEntry> {

	@Override
	public int compare(TreeEntry o1, TreeEntry o2) {
		// TODO Auto-generated method stub
		return CommonUtils.pathToName(o1.getPath()).compareToIgnoreCase(
				CommonUtils.pathToName(o2.getPath()));
	}

}
