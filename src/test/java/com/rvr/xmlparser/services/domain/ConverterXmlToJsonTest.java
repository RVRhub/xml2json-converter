package com.rvr.xmlparser.services.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rvr.xmlparser.exception.XmlToJsonParsingException;
import com.rvr.xmlparser.storage.JsonFileStorage;

@ExtendWith(MockitoExtension.class)
class ConverterXmlToJsonTest
{
	@Mock
	JsonFileStorage jsonFileStorage;

	@Mock
	ImagesExtractor imagesExtractor;

	@Test
	void tryToConvertWithEmptyFileStorage() throws IOException
	{
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(null);
		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson(jsonFileStorage, imagesExtractor);

		XmlToJsonParsingException xmlToJsonParsingException = Assertions
			.assertThrows(XmlToJsonParsingException.class, converterXmlToJson::convert);
		assertTrue(xmlToJsonParsingException.getMessage().contains("Empty file storage"));
	}

	@Test
	void tryToConvertXmlToJsonWithNotExistFile() throws IOException
	{
		File mockFile = mock(File.class);
		when(mockFile.toString()).thenReturn("text.xml");
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(Arrays.array(mockFile));
		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson(jsonFileStorage, imagesExtractor);

		XmlToJsonParsingException xmlToJsonParsingException = Assertions
			.assertThrows(XmlToJsonParsingException.class, converterXmlToJson::convert);
		assertTrue(xmlToJsonParsingException.getMessage().contains("The problem with file: text.xml"));
	}

	@Test
	void tryToConvertXmlToJson() throws IOException
	{
		String filePath = "src/test/resources/storage/3956-coah.xml";

		File mockFile = mock(File.class);
		when(mockFile.toString()).thenReturn(filePath);
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(Arrays.array(mockFile));
		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson(jsonFileStorage, imagesExtractor);

		converterXmlToJson.convert();

		ArgumentCaptor<JSONObject> jsonObjectCaptor = ArgumentCaptor.forClass(JSONObject.class);
		verify(jsonFileStorage).save(any(), jsonObjectCaptor.capture());
		assertNotNull(jsonObjectCaptor.getValue().get("content"));

		ArgumentCaptor<JSONObject> jsonObjectForImageCaptor = ArgumentCaptor.forClass(JSONObject.class);
		verify(imagesExtractor).extractImagesFromCoahObject(jsonObjectForImageCaptor.capture());
		assertNotNull(jsonObjectCaptor.getValue().get("content"));
	}
}
