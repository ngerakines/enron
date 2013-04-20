package com.socklabs.enron;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 4/20/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultEmailTree extends AbstractEmailTree<List<String>> {

	public void appendValue(final CharSequence key, final String value) {
		final Optional<Node<List<String>>> nodeOptional = getNode(key, true);
		if (!nodeOptional.isPresent()) {
			throw new RuntimeException("Could not find or create node for sequence " + key.toString());
		}
		final Node<List<String>> node = nodeOptional.get();
		final Optional<List<String>> optional = node.getValue();
		if (optional.isPresent()) {
			final List<String> nodeValue = optional.get();
			nodeValue.add(value);
			return;
		}
		final List<String> values = new ArrayList<String>();
		values.add(value);
		node.setValue(values);
	}

}
