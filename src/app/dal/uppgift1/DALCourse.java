package app.dal.uppgift1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import app.database.mssql.uppgift1.Connector;
import app.model.uppgift1.Course;
import app.database.DbUtils;
import app.database.PsFiller;
import app.database.fields.uppgift1.DbCourse;
import app.database.fields.uppgift1.DbStudied;
import app.database.fields.uppgift1.DbStudying;

public class DALCourse {

	public static HashMap<String, Object> findCourseInfoByCourseID(String courseID) {
		
		StringBuilder query = new StringBuilder();
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		int indexCounter = 1;

		query.append("select * from " + DbCourse.TABLENAME + " where " + DbCourse.CODE + " = ?");
		queryFiller.put(indexCounter++, courseID);

		HashMap<String, Object> valuesFromDatabase = new HashMap<String, Object>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();


			while (rs.next()) {
				valuesFromDatabase.put(DbCourse.CODE.getDbName(), rs.getString(DbCourse.CODE.getDbName()));
				valuesFromDatabase.put(DbCourse.NAME.getDbName(), rs.getString(DbCourse.NAME.getDbName()));
				valuesFromDatabase.put(DbCourse.POINTS.getDbName(), rs.getString(DbCourse.POINTS.getDbName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		if (valuesFromDatabase.get(DbCourse.CODE.getDbName()) != null) {
			try {
				query.setLength(0);
				queryFiller.clear();
				indexCounter = 1;

				String grade = DbStudied.GRADE.getDbName();

				// Average grade
				query.append("select case when avg(cast(l." + grade + " as float)) is null or (avg(" + grade
						+ ") = '' and avg(" + grade + ") != 0) then -1 else round(avg(cast(" + grade
						+ " as float)), 1) end as data from " + DbStudied.TABLENAME + " l where " + DbStudied.CODE
						+ " = ?");
				queryFiller.put(indexCounter++, courseID);

				query.append(" union all ");

				// Percentage that has achieved the top grade
				query.append("select round(cast((select count(*) from " + DbStudied.TABLENAME);
				query.append(" where " + DbStudied.CODE + " = ? and " + DbStudied.GRADE + " = 5 group by "
						+ DbStudied.GRADE + ") as float) ");
				queryFiller.put(indexCounter++, courseID);
				query.append("/ cast((select count(*) from " + DbStudied.TABLENAME + " where " + DbStudied.CODE
						+ " = ?) as float) * 100, 1)");
				queryFiller.put(indexCounter++, courseID);

				query.append(" union all ");

				// Number studying
				query.append("select count(" + DbStudying.CODE + ") from " + DbStudying.TABLENAME + " where "
						+ DbStudying.CODE + " = ?");
				queryFiller.put(indexCounter++, courseID);

				query.append(" union all ");

				// Number studied
				query.append("select count(" + DbStudied.CODE + ") from " + DbStudied.TABLENAME + " where "
						+ DbStudied.CODE + " = ?");
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");

				// Percentages of each grade compared to all
				// Grade 0
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 0) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");
				// Grade 1
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 1) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");
				// Grade 2
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 2) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");
				// Grade 3
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 3) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");
				// Grade 4
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 4) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				query.append(" union all ");
				// Grade 5
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.CODE + " = k." + DbCourse.CODE + " and " + DbStudied.GRADE + " = 5) as float) / count(*) from " + DbCourse.TABLENAME + " k where " + DbCourse.CODE + " =  ? group by " + DbCourse.CODE);
				queryFiller.put(indexCounter++, courseID);
				
				con = Connector.getConnection();
				ps = con.prepareStatement(query.toString());
				PsFiller.fill(ps, queryFiller);
				rs = ps.executeQuery();

				rs.next();
				valuesFromDatabase.put("avgGrade", rs.getDouble(1));
				rs.next();
				valuesFromDatabase.put("topGradePercentage", rs.getDouble(1));
				rs.next();
				valuesFromDatabase.put("countStudying", rs.getInt(1));
				rs.next();
				valuesFromDatabase.put("countStudied", rs.getInt(1));

				for (int i = 0; i <= 5; i++) {
					rs.next();
					valuesFromDatabase.put("grade" + i, rs.getDouble(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DbUtils.closeQuietly(rs);
				DbUtils.closeQuietly(ps);
				DbUtils.closeQuietly(con);
			}
		}

		return valuesFromDatabase;
	}

	public static ArrayList<Course> findAllCourses() {
		
		String query = "select k.*, round(cast((select count(*)"
											+ " from " + DbStudied.TABLENAME + " l "
											+ "where l." + DbStudied.CODE + " = k." + DbCourse.CODE
											+ " and l." + DbStudied.GRADE + " != 0 "
											+ "group by l." + DbStudied.CODE + ") as float)"
											
											+ " / "
											
											+ "cast((select count(*) from " + DbStudied.TABLENAME + " l2 "
											+ "where l2." + DbStudied.CODE + " = k." + DbCourse.CODE 
											+ " group by l2." + DbStudied.CODE + ") as float) * 100, 3) throughFlow"
					+ " from " + DbCourse.TABLENAME + " k "
					+ "group by k." + DbCourse.CODE + ", k." + DbCourse.NAME + ", k." + DbCourse.POINTS;
				
		ArrayList<Course> valuesFromDatabase = new ArrayList<Course>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				Course c = new Course(rs.getString(DbCourse.CODE.getDbName()), rs.getString(DbCourse.NAME.getDbName()),
						rs.getInt(DbCourse.POINTS.getDbName()), rs.getDouble("throughFlow"));
				valuesFromDatabase.add(c);
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

	public static HashMap<String, Object> findAllCourseStatistics() {

		StringBuilder query = new StringBuilder();

		query.append("select case when count(*) > 0 then round(100 * cast((select count(*) from " + DbStudied.TABLENAME + " where " + DbStudied.GRADE + " != 0) as float) / cast(count(*) as float), 1)");
		query.append(" else -1 end from " + DbStudied.TABLENAME);
		
		query.append(" union all ");

		query.append("select count(*) from " + DbStudying.TABLENAME);

		query.append(" union all ");

		query.append("select count(*) from " + DbStudied.TABLENAME);
		
		query.append(" union all ");
		
		String studied = DbStudied.TABLENAME.getDbName();
		String grade = DbStudied.GRADE.getDbName();
		
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 0");
		query.append(" union all ");
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 1");
		query.append(" union all ");
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 2");
		query.append(" union all ");
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 3");
		query.append(" union all ");		
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 4");
		query.append(" union all ");
		query.append("select cast((count(" + grade + ")) as float) / cast((select count(" + grade + ") from " + studied + ") as float) from " + studied + " where " + grade + " = 5");
		
		HashMap<String, Object> valuesFromDatabase = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			valuesFromDatabase = new HashMap<String, Object>();

			rs.next();
			valuesFromDatabase.put("throughFlow", rs.getDouble(1));
			rs.next();
			valuesFromDatabase.put("countStudying", rs.getInt(1));
			rs.next();
			valuesFromDatabase.put("countStudied", rs.getInt(1));
			
			int counter = 0;
			while (rs.next()) {
				valuesFromDatabase.put("grade" + counter++, rs.getDouble(1));
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

	public static int addCourse(Course c) throws SQLException {
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

		String query = "insert into " + DbCourse.TABLENAME + " (" + DbCourse.CODE + ", " + DbCourse.NAME + ", "
				+ DbCourse.POINTS  + ") " + " values (?, ?, ?)";
		queryFiller.put(1, c.getCode());
		queryFiller.put(2, c.getName());
		queryFiller.put(3, c.getPoints());

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			PsFiller.fill(ps, queryFiller);
			
			return ps.executeUpdate();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
					
	}

	public static int deleteCourse(String courseCode) throws SQLException {
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		
		String query = "delete from " + DbCourse.TABLENAME + " where " + DbCourse.CODE + " = ?";
		queryFiller.put(1, courseCode);
		
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			PsFiller.fill(ps, queryFiller);
			return ps.executeUpdate();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
	}
		
	public static int findCoursePoints(String courseCode) {
		
		String query = "select " + DbCourse.POINTS + " from " + DbCourse.TABLENAME + " where " + DbCourse.CODE + " = ?";
		
		int coursePoints = 0;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			con = Connector.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, courseCode);
			rs = ps.executeQuery();
			
			coursePoints = (rs.next()) ? rs.getInt(1) : -1;
						
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return coursePoints;
	}
}
