package com.rvr.xmlparser.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.rvr.xmlparser.services.XmlToJsonService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class XmlToJsonControllerTest
{
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	XmlToJsonService xmlToJsonService;

	@Test
	public void testPostConvert() throws Exception
	{
		String result = restTemplate.postForObject("/convert", new HttpEntity<>(""), String.class);

		assertThat(result).isEqualTo("All xml documents was converted to JSON files.");
		verify(xmlToJsonService).convertAllXmlDocumentsToJson();
	}

	@Test
	public void testGetSingleJsonObject() throws Exception
	{
		JSONArray obj1 = new JSONArray()
			.put("abc")
			.put(2);
		when(xmlToJsonService.getAllJsonObject()).thenReturn(obj1.toList());

		ResponseEntity<String> response = restTemplate.getForEntity("/result", String.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isEqualTo("[\"abc\",2]");
	}

}
