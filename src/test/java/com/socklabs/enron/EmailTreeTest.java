package com.socklabs.enron;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 4/20/13
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailTreeTest {

	@Test
	public void createTree() {
		final DefaultEmailTree tree = new DefaultEmailTree();
		Assert.assertNotNull(tree);
	}

	@Test
	public void set() {
		final DefaultEmailTree tree = new DefaultEmailTree();
		tree.setValue("foo", new ArrayList<String>());

		final AbstractEmailTree.Node<List<String>> root = tree.getRoot();
		Assert.assertNotNull(root);
		Assert.assertEquals(root.getChildren().size(), 1);
		Assert.assertEquals(tree.getValue("foo").isPresent(), true);
	}

}
