package com.socklabs.enron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(TreeLoader.class);
	private static final Charset charset = Charset.forName("UTF-8");

	private final DefaultEmailTree emailTree;

	public TreeLoader(final DefaultEmailTree emailTree) {
		this.emailTree = emailTree;
	}

	public void load(final String path) {
		LOGGER.debug("Loading path {}", path);
		final List<String> files;
		try {
			files = walk(path);
			for (final String file : files) {
				// LOGGER.info("FOUND {}", file);
				index(file);
			}
		} catch (IOException e) {
			LOGGER.error("oops", e);
		}
	}

	private List<String> walk(final String path) throws IOException {
		final Path startPath = Paths.get(path);
		final FileVisitor fileVisitor = new FileVisitor(path);
		Files.walkFileTree(startPath, fileVisitor);
		return fileVisitor.getPaths();
	}

	private void index(final String fileName) {
		final Path path = FileSystems.getDefault().getPath(fileName);
		String messageId = "";
		try {
			final BufferedReader reader = Files.newBufferedReader(path, charset);
			String line = null;
			boolean firstEmpty = false;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Message-ID:")) {
					messageId = line.substring("Message-ID: ".length());
				}
				if (line.isEmpty()) {
					firstEmpty = true;
				}
				if (firstEmpty) {
					final List<String> words = parseWords(line);
					for (final String word : words) {
						emailTree.appendValue(word, messageId);
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			LOGGER.error("Could not open file " + fileName, e);
		}
	}

	private List<String> parseWords(final String line) {
		final List<String> words = new ArrayList<String>();
		for (final String word : line.split(" ")) {
			words.add(word);
		}
		return words;
	}

}