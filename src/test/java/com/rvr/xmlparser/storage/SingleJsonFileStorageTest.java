package com.rvr.xmlparser.storage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class SingleJsonFileStorageTest
{
	@Mock
	Resource resultResourceDir;

	@Test
	void saveJsonArrayToResultStorageTest() throws IOException
	{
		JSONArray jsonArray = mock(JSONArray.class);
		when(jsonArray.toString(4)).thenReturn("[\"abc\",2]");

		File mockFile = mock(File.class);
		when(mockFile.getAbsolutePath()).thenReturn("./src/test/resources/result");
		when(resultResourceDir.getFile()).thenReturn(mockFile);

		SingleJsonFileStorage singleJsonFileStorage = new SingleJsonFileStorage(resultResourceDir);
		singleJsonFileStorage.save(jsonArray);

		verify(resultResourceDir).getFile();
		verify(jsonArray).toString(4);
	}
}
