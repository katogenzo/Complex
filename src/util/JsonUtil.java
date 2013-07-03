package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	@SuppressWarnings("unchecked")
	public static List<LinkedHashMap<String, Object>> JsonToList(String JsonString) {
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<LinkedHashMap<String, Object>> list = null;
		try {
			list = mapper.readValue(JsonString, ArrayList.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

}
