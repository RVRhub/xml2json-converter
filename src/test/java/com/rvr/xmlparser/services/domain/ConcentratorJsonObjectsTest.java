package com.rvr.xmlparser.services.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.rvr.xmlparser.exception.ConcentratorJsonException;
import com.rvr.xmlparser.exception.XmlToJsonParsingException;
import com.rvr.xmlparser.storage.JsonFileStorage;
import com.rvr.xmlparser.storage.SingleJsonFileStorage;

@ExtendWith(MockitoExtension.class)
class ConcentratorJsonObjectsTest
{
	@Mock
	JsonFileStorage jsonFileStorage;

	@Mock
	SingleJsonFileStorage singleJsonFileStorage;

	@Autowired
	UniqueObjectManager uniqueObjectManager;

	@Test
	void concentratorJsonObjectsWithEmptyFileStorage() throws IOException
	{
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(null);
		ConcentratorJsonObjects concentratorJsonObjects
			= new ConcentratorJsonObjects(jsonFileStorage, singleJsonFileStorage, uniqueObjectManager);

		ConcentratorJsonException concentratorJsonException = Assertions
			.assertThrows(ConcentratorJsonException.class, concentratorJsonObjects::mergeToSingleObject);
		assertTrue(concentratorJsonException.getMessage().contains("Empty file storage"));
	}

	@Test
	void concentratorJsonObjectsWithNotExistFile() throws IOException
	{
		String filePath = "test.json";

		File mockFile = mock(File.class);
		when(mockFile.toString()).thenReturn(filePath);
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(Arrays.array(mockFile));
		ConcentratorJsonObjects concentratorJsonObjects
			= new ConcentratorJsonObjects(jsonFileStorage, singleJsonFileStorage, uniqueObjectManager);

		ConcentratorJsonException concentratorJsonException = Assertions
			.assertThrows(ConcentratorJsonException.class, concentratorJsonObjects::mergeToSingleObject);
		assertTrue(concentratorJsonException.getMessage().contains("Can't handle object with file name: test.json"));
	}

	@Test
	void concentratorJsonObjects() throws IOException
	{
		String filePath = "src/test/resources/storage/3956-coah.json";

		File mockFile = mock(File.class);
		when(mockFile.toString()).thenReturn(filePath);
		when(jsonFileStorage.getAllFilesFromStorage()).thenReturn(Arrays.array(mockFile));
		ConcentratorJsonObjects concentratorJsonObjects
			= new ConcentratorJsonObjects(jsonFileStorage, singleJsonFileStorage, new UniqueObjectManager());

		List<Object> objects = concentratorJsonObjects.mergeToSingleObject();

		assertEquals(1, objects.size());

		ArgumentCaptor<JSONArray> jsonArrayArgumentCaptor = ArgumentCaptor.forClass(JSONArray.class);
		verify(singleJsonFileStorage).save(jsonArrayArgumentCaptor.capture());
		assertEquals(1, jsonArrayArgumentCaptor.getValue().length());
		JSONObject firstObject = (JSONObject) jsonArrayArgumentCaptor.getValue().get(0);
		assertNotNull(firstObject.get("content"));
	}
}
