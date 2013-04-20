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

	public void add(CharSequence str, final T value) {
		Node<T> node = root;
		Node<T> previousNode = null;
		int i = 0;
		while (i < str.length()) {
			previousNode = node;
			final Optional<Node<T>> optionalNode = node.getChildren(str.charAt(i));
			if (!optionalNode.isPresent()) {
				break;
			}
			i++;
		}

		if (i < str.length()) {
			node = previousNode;
			while (i < str.length()) {
				if (node != null) {
					node = node.addChild(str.charAt(i++));
				}
			}
		}
		if (node != null) {
			node.setValue(value);
		}

	}

	private static class Node<T> {
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
