package com.rvr.xmlparser.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SingleJsonFileStorage
{
	private final Resource resultResourceDir;

	public SingleJsonFileStorage(@Value("classpath:result/") Resource resultResourceDir)
	{
		this.resultResourceDir = resultResourceDir;
	}

	public void save(JSONArray singleJsonFile) throws IOException
	{
		var singleResult = Paths.get(resultResourceDir.getFile().getAbsolutePath() + "/singleResult.json");
		Files.write(singleResult, singleJsonFile.toString(4).getBytes());
	}
}
