package com.rvr.xmlparser.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class JsonFileStorage
{
	public static final String PATH_DELIMITER = "/";
	private final Resource storageResourceDir;

	public JsonFileStorage(@Value("classpath:storage/") Resource storageResourceDir)
	{
		this.storageResourceDir = storageResourceDir;
	}

	public void save(File resourceFile, JSONObject xmlJSONObj) throws IOException
	{
		String newFileName = resourceFile.getName().replace("xml", "json");
		var path = Paths.get(this.storageResourceDir.getFile().getAbsolutePath() + PATH_DELIMITER + newFileName);
		Files.write(path, xmlJSONObj.toString(4).getBytes());
	}

	public File[] getAllFilesFromStorage() throws IOException
	{
		return storageResourceDir.getFile().listFiles();
	}
}
