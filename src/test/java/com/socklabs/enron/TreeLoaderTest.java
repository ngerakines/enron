package com.socklabs.enron;

import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 4/20/13
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeLoaderTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TreeLoaderTest.class);

	@Test
	public void load() {
		final DefaultEmailTree tree = new DefaultEmailTree();
		final TreeLoader treeLoader = new TreeLoader(tree);
		treeLoader.load("/home/ngerakines/projects/enron/data");
		Assert.assertNotNull(tree);
		Map<String, List<String>> mapView = tree.asMap();
		// NKG: Big assumption, whatever sample data being used has more than 25 words that can be indexed.
		Assert.assertTrue(mapView.size() > 25);

		Map<String, AbstractEmailTree.Node<List<String>>> nodeMapView = tree.asNodeMap();
		Assert.assertTrue(nodeMapView.size() > 25);

		EmailTreeManager emailTreeManager = new EmailTreeManager();
		emailTreeManager.dump(tree, "/home/ngerakines/tmp/foo.data");
	}

}
