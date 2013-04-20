package com.socklabs.enron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 4/20/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailTreeManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTreeManager.class);

	public void dump(EmailTree<List<String>> tree, final String path) {
		dumpSimple(tree, path);
	}

	private void dumpSimple(final EmailTree<List<String>> emailTree, final String path) {
		final Map<String, List<String>> map = emailTree.asMap();
		// NKG: And this is where I leave off for now.
	}

	public List<String> search(final String word, final String file) {
		return new ArrayList<String>();
	}

}
