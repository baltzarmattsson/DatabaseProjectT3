package app.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class PsFiller {
	
	public static PreparedStatement fill(PreparedStatement ps, HashMap<Integer, Object> queryFiller) throws SQLException {
			
		if (queryFiller != null) {
			for (Map.Entry<Integer, Object> map : queryFiller.entrySet()) {
				int index = map.getKey();
				Object value = map.getValue();
				if (value instanceof String) {
					ps.setString(index, (String) value);
				} else if (value instanceof Integer) {
					ps.setInt(index, (int) value);
				}
			}
		}
		return ps;
	}
}
