package com.rvr.xmlparser.storage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class ImagesStorageTest
{
	@Mock
	Resource imagesResourceDir;

	@Test
	void saveImageTest() throws IOException
	{
		File mockFile = mock(File.class);
		when(mockFile.getAbsolutePath()).thenReturn("./src/test/resources/images");
		when(imagesResourceDir.getFile()).thenReturn(mockFile);

		String filePath = "file:./src/test/resources/images/testImage.jpg";

		ImagesStorage imagesStorage = new ImagesStorage(imagesResourceDir);
		imagesStorage.save(filePath);

		verify(imagesResourceDir).getFile();
	}

	@Test
	void saveImageTestWithNotExistUrl() throws IOException
	{
		String filePath = "testImage.jpg";
		ImagesStorage imagesStorage = new ImagesStorage(imagesResourceDir);

		imagesStorage.save(filePath);
	}
}
