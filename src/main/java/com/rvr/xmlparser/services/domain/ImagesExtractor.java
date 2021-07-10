package com.rvr.xmlparser.services.domain;

import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.rvr.xmlparser.services.SafelyReadJsonObject;
import com.rvr.xmlparser.storage.ImagesStorage;

/**
 * This class saves images that defined in section content->hotel->images
 * from Coah Json Object
 */
@Service
public class ImagesExtractor implements SafelyReadJsonObject
{
	private final ImagesStorage imagesStorage;

	public ImagesExtractor(ImagesStorage imagesStorage)
	{
		this.imagesStorage = imagesStorage;
	}

	public void extractImagesFromCoahObject(JSONObject xmlJSONObj)
	{
		Optional.ofNullable(safeRead(() -> xmlJSONObj.getJSONObject("content")))
			.map(t -> safeRead(() -> t.getJSONObject("hotel")))
			.map(t -> safeRead(() -> t.getJSONObject("images")))
			.map(t -> t.getJSONArray("image"))
			.ifPresent(images -> IntStream.range(0, images.length())
				.mapToObj(index -> ((JSONObject) images.get(index)).optString("url"))
				.forEach(imagesStorage::save));
	}
}
