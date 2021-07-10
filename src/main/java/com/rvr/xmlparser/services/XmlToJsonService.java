package com.rvr.xmlparser.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rvr.xmlparser.services.domain.ConcentratorJsonObjects;
import com.rvr.xmlparser.services.domain.ConverterXmlToJson;

/**
 * This is main service that provide access to xml2json converter
 * And provides the function of combining all JSON objects into one
 */
@Service
public class XmlToJsonService
{
	private final ConverterXmlToJson converterXmlToJson;
	private final ConcentratorJsonObjects concentratorJsonObjects;

	public XmlToJsonService(ConverterXmlToJson converterXmlToJson, ConcentratorJsonObjects concentratorJsonObjects)
	{
		this.converterXmlToJson = converterXmlToJson;
		this.concentratorJsonObjects = concentratorJsonObjects;
	}

	public void convertAllXmlDocumentsToJson()
	{
		converterXmlToJson.convert();
	}

	public List<Object> getAllJsonObject()
	{
		return concentratorJsonObjects.mergeToSingleObject();
	}
}
