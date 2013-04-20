package com.socklabs.enron;

import org.testng.Assert;
import org.testng.annotations.Test;

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

}
