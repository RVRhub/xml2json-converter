package com.rvr.xmlparser.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class JsonFileStorageTest
{
	@Mock
	Resource storageResourceDir;

	@Test
	void getAllFilesFromStorageTest() throws IOException
	{
		File mockFile = mock(File.class);
		when(mockFile.listFiles()).thenReturn(Arrays.array(mockFile));
		when(storageResourceDir.getFile()).thenReturn(mockFile);

		JsonFileStorage jsonFileStorage = new JsonFileStorage(storageResourceDir);
		File[] allFilesFromStorage = jsonFileStorage.getAllFilesFromStorage();

		assertEquals(1, allFilesFromStorage.length);
		verify(storageResourceDir).getFile();
		verify(mockFile).listFiles();
	}

	@Test
	void saveJsonObjectTest() throws IOException
	{
		File resourceFile = mock(File.class);
		when(resourceFile.getName()).thenReturn("test.xml");
		JSONObject jsonObject = mock(JSONObject.class);
		when(jsonObject.toString(4)).thenReturn("{\"test\":\"10\"}");
		File mockFile = mock(File.class);
		when(mockFile.getAbsolutePath()).thenReturn("./src/test/resources/storage");
		when(storageResourceDir.getFile()).thenReturn(mockFile);

		JsonFileStorage jsonFileStorage = new JsonFileStorage(storageResourceDir);
		jsonFileStorage.save(resourceFile, jsonObject);

		verify(storageResourceDir).getFile();
		verify(jsonObject).toString(4);
	}
}
