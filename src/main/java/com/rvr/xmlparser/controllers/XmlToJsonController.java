package com.rvr.xmlparser.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rvr.xmlparser.services.XmlToJsonService;

@RestController
public class XmlToJsonController
{
	private final XmlToJsonService xmlToJsonService;

	public XmlToJsonController(XmlToJsonService xmlToJsonService)
	{
		this.xmlToJsonService = xmlToJsonService;
	}

	@PostMapping("/convert")
	public ResponseEntity<String> convert()
	{
		xmlToJsonService.convertAllXmlDocumentsToJson();

		return new ResponseEntity<>("All xml documents was converted to JSON files.", HttpStatus.ACCEPTED);
	}

	@GetMapping("/result")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Object> getAllJsonObject()
	{
		return xmlToJsonService.getAllJsonObject();
	}
}
