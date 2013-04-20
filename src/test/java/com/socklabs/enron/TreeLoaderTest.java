package com.socklabs.enron;

import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

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
		final EmailTree<List<String>> tree = new EmailTree<List<String>>();
		final TreeLoader treeLoader = new TreeLoader(tree);
		treeLoader.load("/home/ngerakines/projects/enron/data");
		Assert.assertNotNull(tree);
	}

}
