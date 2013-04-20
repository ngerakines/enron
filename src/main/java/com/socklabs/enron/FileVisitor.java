package com.socklabs.enron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple impl of the {@link FileVisitor} interface that simply collects paths matching a pattern.
 */
public class FileVisitor extends SimpleFileVisitor<Path> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileVisitor.class);

	private final List<String> paths = new ArrayList<String>();

	private final String basePath;

	public FileVisitor(final String basePath) {
		this.basePath = basePath;
	}

	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
		LOGGER.info("Visiting {}", file);
		if (!file.toString().contains("_")) {
			paths.add(file.toAbsolutePath().toString());
		}
		Objects.requireNonNull(file);
		Objects.requireNonNull(attrs);
		return FileVisitResult.CONTINUE;
	}

	public List<String> getPaths() {
		return paths;
	}

}
