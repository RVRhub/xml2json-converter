package com.rvr.xmlparser.services.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rvr.xmlparser.exception.XmlToJsonParsingException;
import com.rvr.xmlparser.services.SafelyReadJsonObject;
import com.rvr.xmlparser.storage.JsonFileStorage;

/**
 * This class converts xml files to json files from the file storage.
 * The result is saved in the same directory.
 * <p>
 * Also, before the file is saved, all available images from Coah file type
 * will also be saved to a separate file storage ./resources/main/images
 */
@Service
public class ConverterXmlToJson implements SafelyReadJsonObject
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterXmlToJson.class);
	private final JsonFileStorage jsonFileStorage;
	private final ImagesExtractor imagesExtractor;

	public ConverterXmlToJson(JsonFileStorage jsonFileStorage, ImagesExtractor imagesExtractor)
	{
		this.jsonFileStorage = jsonFileStorage;
		this.imagesExtractor = imagesExtractor;
	}

	public void convert()
	{
		try
		{
			File[] files = jsonFileStorage.getAllFilesFromStorage();
			if (files == null)
			{
				var message = "Empty file storage";
				LOGGER.error(message);
				throw new XmlToJsonParsingException(message);
			}

			Arrays.stream(files)
				.map(file -> file.toString().toLowerCase())
				.filter(f -> f.endsWith("xml"))
				.forEach(this::convertXmlToJson);
		}
		catch (IOException exception)
		{
			var message = "Can't convert files from XML storage.";
			LOGGER.error(message, exception);
			throw new XmlToJsonParsingException(message);
		}
	}

	private void convertXmlToJson(String xmlFileName)
	{
		var resourceFile = new File(xmlFileName);
		try (var xmlIS = new FileInputStream(resourceFile))
		{
			var xmlJSONObj = getJsonObjectFromInputStream(xmlIS);

			imagesExtractor.extractImagesFromCoahObject(xmlJSONObj);

			jsonFileStorage.save(resourceFile, xmlJSONObj);
		}
		catch (IOException e)
		{
			throw new XmlToJsonParsingException(String.format("The problem with file: %s", resourceFile.getName()), e);
		}
	}

	private JSONObject getJsonObjectFromInputStream(FileInputStream xmlIS) throws IOException
	{
		var xmlString = new String(xmlIS.readAllBytes(), StandardCharsets.UTF_8);
		return XML.toJSONObject(xmlString, true);
	}
}
