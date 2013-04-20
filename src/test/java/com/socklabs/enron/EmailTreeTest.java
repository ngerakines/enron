package com.socklabs.enron;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.Graph;

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
		final EmailTree<List<String>> tree = new EmailTree<List<String>>();
		Assert.assertNotNull(tree);
	}

	@Test
	public void set() {
		final EmailTree<List<String>> tree = new EmailTree<List<String>>();
		tree.setValue("foo", new ArrayList<String>());

		final EmailTree.Node<List<String>> root = tree.getRoot();
		Assert.assertNotNull(root);
		Assert.assertEquals(root.getChildren().size(), 1);
		Assert.assertEquals(tree.getValue("foo").isPresent(), true);
	}

}
