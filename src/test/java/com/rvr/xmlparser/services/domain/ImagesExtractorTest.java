package com.rvr.xmlparser.services.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rvr.xmlparser.storage.ImagesStorage;

@ExtendWith(MockitoExtension.class)
class ImagesExtractorTest
{
	@Mock
	ImagesStorage imagesStorage;

	@Test
	void noExtractImagesFromCoahObject() throws IOException
	{
		JSONObject jsonObject = new JSONObject("{\"test\":\"10\"}");

		ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
		imagesExtractor.extractImagesFromCoahObject(jsonObject);

		verify(imagesStorage, never()).save(any());
	}

	@Test
	void extractImagesFromCoahObject() throws IOException
	{
		JSONObject jsonObject = new JSONObject("{\n"
			+ "  \"content\": {\n"
			+ "    \"hotel\": {\n"
			+ "      \"images\": {\n"
			+ "        \"image\": [\n"
			+ "          {\n"
			+ "            \"url\": \"https://storage.googleapis.com/thumbor.prod.promaterial.com/trial_task/coah/3956/image001.jpg\",\n"
			+ "          }\n"
			+ "        ]\n"
			+ "      }\n"
			+ "    }\n"
			+ "  }\n"
			+ "}\n");

		ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
		imagesExtractor.extractImagesFromCoahObject(jsonObject);

		verify(imagesStorage).save(eq("https://storage.googleapis.com/thumbor.prod.promaterial.com/trial_task/coah/3956/image001.jpg"));
	}
}
