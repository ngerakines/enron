package com.socklabs.enron;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 4/20/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailTreeManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTreeManager.class);

	public void dump(DefaultEmailTree tree, final String path) {
		dumpSimple(tree, path);
	}

	private void dumpSimple(final DefaultEmailTree emailTree, final String path) {
		final Map<String, AbstractEmailTree.Node<List<String>>> map = emailTree.asNodeMap();
		final Map<String, FileNode> fileNodes = new HashMap<String, FileNode>();
		for (final Map.Entry<String, AbstractEmailTree.Node<List<String>>> entry : map.entrySet()) {
			final FileNode fileNode = new FileNode();
			final AbstractEmailTree.Node<List<String>> treeNode = entry.getValue();
			fileNode.setKey(entry.getKey());
			final Optional<List<String>> optional = treeNode.getValue();
			if (optional.isPresent()) {
				fileNode.setValues(optional.get());
			}
			final List<String> children = new ArrayList<String>();
			for (Map.Entry<Character, AbstractEmailTree.Node<List<String>>> ignored : treeNode.getChildren().entrySet()) {
				children.add(ignored.getValue().getFullKey().get());
			}
			fileNode.setChildren(children);
			fileNodes.put(entry.getKey(), fileNode);
		}
		final List<String> keys = getKeys(fileNodes);
		int position = 0;
		for (final String key : keys) {
			final FileNode fileNode = fileNodes.get(key);
			if (fileNode == null) {
				throw new RuntimeException();
			}
			fileNode.setPosition(position);
			position += fileNode.serializedSize();
		}
		for (final String key : keys) {
			final FileNode fileNode = fileNodes.get(key);
			if (fileNode == null) {
				throw new RuntimeException();
			}

			if (key.length() > 0) {
				final String parent = key.substring(0, key.length() - 1);
				final FileNode parentFileNode = fileNodes.get(parent);
				if (parentFileNode == null) {
					throw new RuntimeException();
				}
				fileNode.setParentPosition(parentFileNode.getPosition());
			}

			final List<Integer> childPositions = new ArrayList<Integer>();
			for (final String child : fileNode.getChildren()) {
				final FileNode childFileNode = fileNodes.get(child);
				if (childFileNode == null) {
					throw new RuntimeException();
				}
				childPositions.add(childFileNode.getPosition());
			}
			fileNode.setChildrenPositions(childPositions);
		}

		int count = 0;
		try {
			final OutputStream outputStream = new FileOutputStream(path);
			for (final String key : keys) {
				final FileNode fileNode = fileNodes.get(key);
				if (fileNode == null) {
					throw new RuntimeException();
				}
				byte[] bytes = fileNode.serialize();
				outputStream.write(bytes);
				if (count++ % 25 == 0) {
					LOGGER.info("Writing {} to position {} with parent at {} and children at {}", fileNode.getKey(), fileNode.getPosition(), fileNode.getParentPosition(), fileNode.getChildrenPositions());
				}
			}
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getKeys(Map<String, FileNode> fileNodes) {
		List<String> keys = new ArrayList<String>(fileNodes.keySet());
		Collections.sort(keys);
		return keys;
	}

	public List<String> search(final String word, final String file) {
		return new ArrayList<String>();
	}

	private static class FileNode {
		private int position = 0;
		private int parentPosition = 0;
		private List<String> children = new ArrayList<String>();
		private List<Integer> childrenPositions = new ArrayList<Integer>();
		private String key = "";
		private List<String> values = new ArrayList<String>();

		private int getPosition() {
			return position;
		}

		private void setPosition(int position) {
			this.position = position;
		}

		private int getParentPosition() {
			return parentPosition;
		}

		private void setParentPosition(int parentPosition) {
			this.parentPosition = parentPosition;
		}

		private String getKey() {
			return key;
		}

		private void setKey(String key) {
			this.key = key;
		}

		private List<String> getValues() {
			return values;
		}

		private List<String> getChildren() {
			return children;
		}

		private void setChildren(List<String> children) {
			this.children = children;
		}

		private List<Integer> getChildrenPositions() {
			return childrenPositions;
		}

		private void setChildrenPositions(List<Integer> childrenPositions) {
			this.childrenPositions = childrenPositions;
		}

		private void setValues(List<String> values) {
			this.values = values;
		}

		public int serializedSize() {
			int size = 0;
			size += Integer.SIZE; // position
			size += Integer.SIZE; // parent position
			size += Integer.SIZE; // number of children
			size += childrenPositions.size() * Integer.SIZE;
			size += Integer.SIZE; // key length
			size += key.getBytes().length;
			size += Integer.SIZE; // number of values
			for (final String value : values) {
				size += Integer.SIZE; // length of string
				size += value.getBytes().length;
			}
			return size;
		}

		public byte[] serialize() {
			int size = serializedSize();
			final ByteBuffer byteBuffer = ByteBuffer.allocate(size);
			byteBuffer.putInt(position);
			byteBuffer.putInt(parentPosition);
			byteBuffer.putInt(childrenPositions.size());
			for (final Integer childPosition : childrenPositions) {
				byteBuffer.putInt(childPosition);
			}
			byteBuffer.putInt(key.getBytes().length);
			byteBuffer.put(key.getBytes());
			byteBuffer.putInt(values.size());
			for (final String value : values) {
				byteBuffer.putInt(value.getBytes().length);
				byteBuffer.put(value.getBytes());
			}
			return byteBuffer.array();
		}
	}

}
