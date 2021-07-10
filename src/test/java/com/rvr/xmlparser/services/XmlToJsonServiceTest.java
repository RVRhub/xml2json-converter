package com.rvr.xmlparser.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rvr.xmlparser.services.domain.ConcentratorJsonObjects;
import com.rvr.xmlparser.services.domain.ConverterXmlToJson;

@ExtendWith(MockitoExtension.class)
class XmlToJsonServiceTest
{
	@Mock
	ConverterXmlToJson converterXmlToJson;

	@Mock
	ConcentratorJsonObjects concentratorJsonObjects;

	@Test
	void testGetAllJsonObject() throws IOException
	{
		JSONArray obj1 = new JSONArray()
			.put("abc")
			.put(2);
		when(concentratorJsonObjects.mergeToSingleObject()).thenReturn(obj1.toList());
		XmlToJsonService xmlToJsonService = new XmlToJsonService(converterXmlToJson, concentratorJsonObjects);

		List<Object> allJsonObject = xmlToJsonService.getAllJsonObject();

		assertThat(allJsonObject).isEqualTo(obj1.toList());
		verify(concentratorJsonObjects).mergeToSingleObject();
	}

	@Test
	void testConvertAllXmlDocumentsToJson() throws IOException
	{
		XmlToJsonService xmlToJsonService = new XmlToJsonService(converterXmlToJson, concentratorJsonObjects);

		xmlToJsonService.convertAllXmlDocumentsToJson();

		verify(converterXmlToJson).convert();
	}
}
