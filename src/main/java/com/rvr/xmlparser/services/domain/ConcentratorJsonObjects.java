package com.rvr.xmlparser.services.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rvr.xmlparser.exception.ConcentratorJsonException;
import com.rvr.xmlparser.storage.JsonFileStorage;
import com.rvr.xmlparser.storage.SingleJsonFileStorage;

/**
 * This class collect all json files from file store to one single json file.
 * <p>
 * The result is stored to file storage in ./resources/main/result
 * And return as List of JSONObject's.
 */
@Service
public class ConcentratorJsonObjects
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConcentratorJsonObjects.class);

	private final JsonFileStorage jsonFileStorage;
	private final SingleJsonFileStorage singleJsonFileStorage;
	private final UniqueObjectManager uniqueObjectManager;

	public ConcentratorJsonObjects(JsonFileStorage jsonFileStorage, SingleJsonFileStorage singleJsonFileStorage,
		UniqueObjectManager uniqueObjectManager)
	{
		this.jsonFileStorage = jsonFileStorage;
		this.singleJsonFileStorage = singleJsonFileStorage;
		this.uniqueObjectManager = uniqueObjectManager;
	}

	public List<Object> mergeToSingleObject()
	{
		try
		{
			File[] files = jsonFileStorage.getAllFilesFromStorage();
			if (files == null)
			{
				var message = "Empty file storage.";
				LOGGER.error(message);
				throw new ConcentratorJsonException(message);
			}

			var singleJsonFile = new JSONArray();

			Arrays.stream(files)
				.map(p -> p.toString().toLowerCase())
				.filter(f -> f.endsWith("json"))
				.forEach(jsonFileName -> addObjectToSingleJson(singleJsonFile, jsonFileName));

			singleJsonFileStorage.save(singleJsonFile);

			return singleJsonFile.toList();
		}
		catch (IOException e)
		{
			LOGGER.error("Can't get single file.", e);
			throw new ConcentratorJsonException("Can't get single file.");
		}
	}

	private void addObjectToSingleJson(JSONArray singleJsonFile, String jsonFileName)
	{
		try
		{
			var fileInputStream = new FileInputStream(jsonFileName);
			var tokener = new JSONTokener(fileInputStream);
			var object = new JSONObject(tokener);

			if (uniqueObjectManager.isSameObjectNotExist(object))
			{
				singleJsonFile.put(object);
			}

			uniqueObjectManager.markObjectAsAdded(object);
		}
		catch (Exception e)
		{
			var message = String.format("Can't handle object with file name: %s", jsonFileName);
			LOGGER.error(message);
			throw new ConcentratorJsonException(message, e);
		}
	}
}
