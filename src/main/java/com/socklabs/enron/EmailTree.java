package com.socklabs.enron;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EmailTree<T> {

	private Node<T> root;

	public EmailTree() {
		this.root = new Node<T>();
	}

	public void setValue(final CharSequence key, final T value) {
		final Optional<Node<T>> nodeOptional = getNode(key, true);
		if (nodeOptional.isPresent()) {
			final Node<T> node = nodeOptional.get();
			node.setValue(value);
			return;
		}
		throw new RuntimeException("Could not find or create node for sequence " + key.toString());
	}

	public Optional<T> getValue(final CharSequence key) {
		final Optional<Node<T>> nodeOptional = getNode(key, false);
		if (nodeOptional.isPresent()) {
			return nodeOptional.get().getValue();
		}
		return Optional.absent();
	}

	public Optional<Node<T>> getNode(final CharSequence key, final boolean createIfMissing) {
		Node<T> node = root;
		Node<T> lastNode = null;
		int i = 0;
		// NKG: Traverse down to find the deepest node that could match.
		while (i < key.length()) {
			lastNode = node;
			final Optional<Node<T>> optionalNode = node.getChildren(key.charAt(i));
			if (!optionalNode.isPresent()) {
				break;
			} else {
				node = optionalNode.get();
			}
			i++;
		}
		if (createIfMissing) {
			// NKG: If we aren't at max, then we didn't find an exact match and need to go up parent chain to add the node.
			if (i < key.length()) {
				while (i < key.length()) {
					node = node.addChild(key.charAt(i++));
				}
			}
		}
		return Optional.of(node);
	}

	public static class Node<T> {
		private Optional<Character> key;
		private Optional<T> value;
		private Optional<Node<T>> parent;
		private final Map<Character, Node<T>> children = new HashMap<Character, Node<T>>();

		public Node() {
			this.key = Optional.absent();
			this.value = Optional.absent();
			this.parent = Optional.absent();
		}

		public void setValue(@Nonnull final T value) {
			this.value = Optional.<T>of(value);
		}

		public void setKey(@Nonnull final Character key) {
			this.key = Optional.of(key);
		}

		public void setParent(@Nonnull final Node<T> node) {
			this.parent = Optional.of(node);
		}

		public Optional<Character> getKey() {
			return key;
		}

		public Optional<T> getValue() {
			return value;
		}

		public Optional<Node<T>> getParent() {
			return parent;
		}

		public Map<Character, Node<T>> getChildren() {
			return children;
		}

		public Optional<Node<T>> getChildren(@Nonnull final Character key) {
			@Nullable final Node<T> node = children.get(key);
			if (node != null) {
				return Optional.of(node);
			}
			return Optional.absent();
		}

		public Node<T> addChild(final char key) {
			final Node<T> emptyNode = new Node<T>();
			children.put(key, emptyNode);
			return emptyNode;
		}
	}

	public Node<T> getRoot() {
		return root;
	}

}
