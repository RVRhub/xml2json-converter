package com.rvr.xmlparser.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ImagesStorage
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesStorage.class);
	private final Resource imagesResourceDir;

	public ImagesStorage(@Value("classpath:images/") Resource imagesResourceDir)
	{
		this.imagesResourceDir = imagesResourceDir;
	}

	public void save(String url)
	{
		try (InputStream in = new URL(url).openStream())
		{
			var imageFileName = url.substring(url.lastIndexOf('/') + 1);
			Files.copy(in, Paths.get(imagesResourceDir.getFile().getAbsolutePath() + "/" + imageFileName), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			LOGGER.error("Can't save image with url: {}", url, e);
		}
	}
}
