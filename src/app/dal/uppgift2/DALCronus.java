package app.dal.uppgift2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import app.database.DbUtils;
import app.database.mssql.uppgift2.Connector;
import app.model.uppgift2.FetchedMetadataRow;
import app.model.uppgift2.FetchedRow;

public class DALCronus {
	
	public static ArrayList<String> findAllTables(boolean onlyFromEmployeeTables) {
		
		String query = "select * from INFORMATION_SCHEMA.TABLES ";
		if (onlyFromEmployeeTables) query += "where TABLE_NAME like ?";
		ArrayList<String> valuesFromDatabase = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			if (onlyFromEmployeeTables) ps.setString(1, "%AB$Employee%");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				valuesFromDatabase.add(rs.getString("TABLE_NAME"));
			}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return valuesFromDatabase;
		
	}
	
	public static HashMap<Integer, String> findColumnNamesFromTable(String tableName) {
		
		String  query = "select * from [" + tableName + "]";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSetMetaData rsmd = null;
		
		HashMap<Integer, String> columnInfo = null;
		
		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rsmd = ps.executeQuery().getMetaData();
			
			columnInfo = new HashMap<Integer, String>();
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String columnName = rsmd.getColumnName(i);
				columnInfo.put(i, columnName);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return columnInfo;
		
	}
	
	public static ArrayList<FetchedRow> findAllContentFromTable(String tableName) {
		
		String query = "select * from [" + tableName + "]";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<FetchedRow> fetchedRows = null;
		
		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			fetchedRows = new ArrayList<FetchedRow>();
			FetchedRow fr = null;
			while (rs.next()) {
				String att1, att2, att3, att4, att5;
				
				att1 = isInRange(rs, 1) ? rs.getString(1) : "";
				att2 = isInRange(rs, 2) ? rs.getString(2) : "";
				att3 = isInRange(rs, 3) ? rs.getString(3) : "";
				att4 = isInRange(rs, 4) ? rs.getString(4) : "";
				att5 = isInRange(rs, 5) ? rs.getString(5) : "";
				
				fr = new FetchedRow(att1, att2, att3, att4, att5);
				fetchedRows.add(fr);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return fetchedRows;
	}
	
	public static ArrayList<FetchedRow> findAllContentByFunction(String chosenFunction) {
		
		String query = getQueryEquivalentToFunction(chosenFunction);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList<FetchedRow> fetchedRows = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			fetchedRows = new ArrayList<FetchedRow>();
			FetchedRow fr = null;
			while (rs.next()) {
				String att1, att2, att3, att4, att5, att6, att7, att8, att9, att10;
				
				att1 = isInRange(rs, 1) ? rs.getString(1) : "";
				att2 = isInRange(rs, 2) ? rs.getString(2) : "";
				att3 = isInRange(rs, 3) ? rs.getString(3) : "";
				att4 = isInRange(rs, 4) ? rs.getString(4) : "";
				att5 = isInRange(rs, 5) ? rs.getString(5) : "";
				att6 = isInRange(rs, 6) ? rs.getString(6) : "";
				att7 = isInRange(rs, 7) ? rs.getString(7) : "";
				att8 = isInRange(rs, 8) ? rs.getString(8) : "";
				att9 = isInRange(rs, 9) ? rs.getString(9) : "";
				att10 = isInRange(rs, 10) ? rs.getString(10) : "";
				
				fr = new FetchedRow(att1, att2, att3, att4, att5, att6, att7, att8, att9, att10);
				fetchedRows.add(fr);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		return fetchedRows;
		
	}
	
	public static HashMap<Integer, String> findColumnNamesByFunction(String chosenFunction) {

		String query = getQueryEquivalentToFunction(chosenFunction);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSetMetaData rsmd = null;
		HashMap<Integer, String> columnInfo = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rsmd = ps.executeQuery().getMetaData();

			columnInfo = new HashMap<Integer, String>();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String columnName = rsmd.getColumnName(i);
				columnInfo.put(i, columnName);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		return columnInfo;

	}
	
	private static String getQueryEquivalentToFunction(String chosenFunction) {
		String query = "";

		switch (chosenFunction) {
		case "Nycklar":
			query = "select distinct constraint_name from INFORMATION_SCHEMA.KEY_COLUMN_USAGE";
			break;
		case "Indexes":
			query = "select * from sys.indexes where name like '%CRONUS%'";
			break;
		case "Table constraints":
			query = "select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS";
			break;
		case "Samtliga tabeller":
			query = "select name from sys.tables";
			break;
		case "Samtliga kolumner i Employee":
			query = "select column_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'CRONUS Sverige AB$Employee'";
			
			// alt 2
			
//			 query = "select c.name from sys.columns c inner join sys.tables t on "
//			 + "c.object_id = t.object_id and t.name = 'CRONUS Sverige AB$Employee';";
			break;
		case "Tabell med flest rader":
			query = "select top 1 tablename from (select object_name(object_id)tablename, "
					+ "st.row_count as antal from sys.dm_db_partition_stats st where index_id < 2) "
					+ "x where tablename like 'CRONUS%' " + "group by tablename, antal order by antal desc;";
			break;
		}
		
		return query;
		
	}
	
	public static ArrayList<FetchedMetadataRow> findTableMetaDataRowsFromTable(String tableName) {
		
		String query = "select TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME like '%" + tableName + "'";
		
		ArrayList<FetchedMetadataRow> fetchedRows = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			fetchedRows = new ArrayList<FetchedMetadataRow>();
			
			while (rs.next()) {
				
				FetchedMetadataRow row = new FetchedMetadataRow(rs.getString("TABLE_SCHEMA"), rs.getString("TABLE_NAME"), rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), rs.getString("COLUMN_DEFAULT"), 
																rs.getString("IS_NULLABLE"), rs.getString("DATA_TYPE"), rs.getInt("CHARACTER_MAXIMUM_LENGTH"), rs.getInt("CHARACTER_OCTET_LENGTH"));
				fetchedRows.add(row);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return fetchedRows;
		
	}
	
	private static boolean isInRange(ResultSet rs, int index) throws SQLException {
		return rs.getMetaData().getColumnCount() >= index;
	}
	
}
