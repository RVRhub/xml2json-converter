package com.rvr.xmlparser.services.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.rvr.xmlparser.services.SafelyReadJsonObject;

/**
 * This service allows avoiding duplicate of objects for a single JSON object.
 */
@Service
@RequestScope
public class UniqueObjectManager implements SafelyReadJsonObject
{
	private final Set<String> uniqGiataObjectInJson = new HashSet<>();
	private final Set<String> uniqCoahObjectInJson = new HashSet<>();

	public boolean isSameObjectNotExist(JSONObject jsonObject)
	{
		return !(getIdFromCoahObject(jsonObject).stream().anyMatch(uniqCoahObjectInJson::contains) ||
			getIdFromGiataObject(jsonObject).stream().anyMatch(uniqGiataObjectInJson::contains));
	}

	public void markObjectAsAdded(JSONObject jsonObject)
	{
		getIdFromCoahObject(jsonObject)
			.ifPresent(uniqCoahObjectInJson::add);

		getIdFromGiataObject(jsonObject)
			.ifPresent(uniqGiataObjectInJson::add);
	}

	private Optional<String> getIdFromGiataObject(JSONObject jsonObject)
	{
		return Optional.ofNullable(safeRead(() -> jsonObject.getJSONObject("result")))
			.map(t -> safeRead(() -> t.getJSONObject("data")))
			.map(t -> safeRead(() -> t.getJSONObject("GeoData")))
			.map(t -> t.getString("GiataID"));
	}

	private Optional<String> getIdFromCoahObject(JSONObject jsonObject)
	{
		return Optional.ofNullable(safeRead(() -> jsonObject.getJSONObject("content")))
			.map(t -> safeRead(() -> t.getJSONObject("hotel")))
			.map(t -> t.getString("giata_id"));
	}
}
