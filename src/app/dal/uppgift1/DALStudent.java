package app.dal.uppgift1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import app.database.mssql.uppgift1.Connector;
import app.model.uppgift1.Entry;
import app.model.uppgift1.Student;
import app.model.uppgift1.StudiedEntry;
import app.model.uppgift1.StudyingEntry;
import app.database.DbUtils;
import app.database.PsFiller;
import app.database.fields.uppgift1.DbCourse;
import app.database.fields.uppgift1.DbStudent;
import app.database.fields.uppgift1.DbStudied;
import app.database.fields.uppgift1.DbStudying;

public class DALStudent {

	public static ArrayList<Student> findAllStudentsByCourseID(String courseID) {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		StringBuilder query = new StringBuilder();
		int indexCounter = 1; 
		
		// Currently reading students (studying)
		query.append("select s.*, 'Läser' as Status from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudying.TABLENAME + " l on l." + DbStudying.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudying.PNUMBER);
		query.append(" where k." + DbCourse.CODE + " = ?");
		queryFiller.put(indexCounter++, courseID);

		query.append(" union all ");

		// Finished students (studied)
		query.append("select s.*, concat('Betyg ', l." + DbStudied.GRADE + ") from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudied.TABLENAME + " l on l." + DbStudied.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudied.PNUMBER);
		query.append(" where k." + DbCourse.CODE + " = ?");
		queryFiller.put(indexCounter++, courseID);

		ArrayList<Student> valuesFromDatabase = new ArrayList<Student>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();

			while (rs.next()) {

				Student student = new Student(rs.getString(DbStudent.PNUMBER.getDbName()),
						rs.getString(DbStudent.NAME.getDbName()), rs.getString(DbStudent.ADDRESS.getDbName()),
						rs.getString(DbStudent.PHONENBR.getDbName()), rs.getString("status"));
				valuesFromDatabase.add(student);

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

	public static HashMap<String, Object> findStudentInfoByStudentID(String studentID) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("select s." + DbStudent.PNUMBER + ", s." + DbStudent.NAME + ", s." + DbStudent.ADDRESS + ", s."
				+ DbStudent.PHONENBR + " from " + DbStudent.TABLENAME + " s where " + DbStudent.PNUMBER + " = ?");
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		queryFiller.put(1, studentID);

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
				valuesFromDatabase.put(DbStudent.PNUMBER.getDbName(), rs.getString(DbStudent.PNUMBER.getDbName()));
				valuesFromDatabase.put(DbStudent.NAME.getDbName(), rs.getString(DbStudent.NAME.getDbName()));
				valuesFromDatabase.put(DbStudent.ADDRESS.getDbName(), rs.getString(DbStudent.ADDRESS.getDbName()));
				valuesFromDatabase.put(DbStudent.PHONENBR.getDbName(), rs.getString(DbStudent.PHONENBR.getDbName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		if (valuesFromDatabase.get(DbStudent.PNUMBER.getDbName()) != null) {

			try {
				query.setLength(0);
				queryFiller.clear();
				int indexCounter = 1;

				String grade = DbStudied.GRADE.getDbName();

				// Average grade on all courses
				query.append("select " + "case " + "when avg(l." + grade + ") is null then -1 " + "else round(avg(cast("
						+ grade + " as float)), 1) " + "end as data " + "from " + DbStudied.TABLENAME + " l " + "where "
						+ DbStudied.PNUMBER + " = ?");
				queryFiller.put(indexCounter++, studentID);

				query.append(" union all ");

				// Number of reading courses
				query.append("select count(" + DbStudying.PNUMBER + ") from " + DbStudying.TABLENAME + " where "
						+ DbStudying.PNUMBER + " = ?");
				queryFiller.put(indexCounter++, studentID);

				query.append(" union all ");

				// Number of courses read
				query.append("select count(" + DbStudied.PNUMBER + ") from " + DbStudied.TABLENAME + " where "
						+ DbStudied.PNUMBER + " = ?");
				queryFiller.put(indexCounter++, studentID);

				query.append(" union all ");

				// Percentages of each grade compared to all
				// Grade 0
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 0) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);
				query.append(" union all ");
				// Grade 1
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 1) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);
				query.append(" union all ");
				// Grade 2
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 2) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);
				query.append(" union all ");
				// Grade 3
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 3) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);
				query.append(" union all ");
				// Grade 4
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 4) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);
				query.append(" union all ");
				// Grade 5
				query.append(
						"select cast((select count(*) from " + DbStudied.TABLENAME + " l1 where l1." + DbStudied.PNUMBER + " = s." + DbStudent.PNUMBER + " and " + DbStudied.GRADE + " = 5) as float) / count(*) from Student s where " + DbStudent.PNUMBER + " = ? group by " + DbStudent.PNUMBER + "");
				queryFiller.put(indexCounter++, studentID);

				con = Connector.getConnection();
				ps = null;
				rs = null;

				ps = con.prepareStatement(query.toString());
				PsFiller.fill(ps, queryFiller);
				rs = ps.executeQuery();

				rs.next();
				valuesFromDatabase.put("avgGrade", rs.getDouble(1));
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

	public static int findCurrentlyReadingPoints(String studentID) {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		
		String query = "select isnull(sum( " + DbCourse.POINTS + "), '0') from " + DbCourse.TABLENAME + " k inner join " + DbStudying.TABLENAME + " l on l." + DbStudying.CODE + " = k." + DbCourse.CODE
				+ " and l." + DbStudying.PNUMBER + " = ?";
		queryFiller.put(1, studentID);
		
		int pointsCurrentlyReading = 0;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();

			while (rs.next()) {
				pointsCurrentlyReading = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}

		return pointsCurrentlyReading;
		
	}
	
	public static ArrayList<Student> findAllStudents() {

		String query = "select s." + DbStudent.PNUMBER + ", s." + DbStudent.NAME + ", s." + DbStudent.ADDRESS + ", s."
				+ DbStudent.PHONENBR + " from " + DbStudent.TABLENAME + " s";

		ArrayList<Student> valuesFromDatabase = new ArrayList<Student>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				Student s = new Student(rs.getString(DbStudent.PNUMBER.getDbName()),
						rs.getString(DbStudent.NAME.getDbName()), rs.getString(DbStudent.ADDRESS.getDbName()),
						rs.getString(DbStudent.PHONENBR.getDbName()));
				valuesFromDatabase.add(s);
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

	public static int addStudent(Student s) throws SQLException {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

		String query = "insert into " + DbStudent.TABLENAME + " (" + DbStudent.PNUMBER + ", " + DbStudent.NAME + ", "
				+ DbStudent.ADDRESS + ", " + DbStudent.PHONENBR + ") " + " values (?, ?, ?, ?)";
		queryFiller.put(1, s.getPersonNumber());
		queryFiller.put(2, s.getName());
		queryFiller.put(3, s.getAddress());
		queryFiller.put(4, s.getPhoneNumber());

		Connection con = Connector.getConnection();
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			return ps.executeUpdate();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}

	}
	
	public static ArrayList<Entry> findAllCourseRelationsByStudentID(String studentID) {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		StringBuilder query = new StringBuilder();
		int indexCounter = 1;

		query.append("select k.*, 'Läser' as Status from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudying.TABLENAME + " l on l." + DbStudying.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudying.PNUMBER);
		query.append(" where s." + DbStudent.PNUMBER + " = ?");
		queryFiller.put(indexCounter++, studentID);

		query.append(" union all ");

		query.append("select k.*, concat('Betyg ', l." + DbStudied.GRADE + ") from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudied.TABLENAME + " l on l." + DbStudied.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudied.PNUMBER);
		query.append(" where s." + DbStudent.PNUMBER + " = ?");
		queryFiller.put(indexCounter++, studentID);

		ArrayList<Entry> valuesFromDatabase = new ArrayList<Entry>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();

			while (rs.next()) {

				Entry rowEntry = new Entry(rs.getString(DbCourse.CODE.getDbName()),
						rs.getString(DbCourse.NAME.getDbName()), rs.getInt(DbCourse.POINTS.getDbName()),
						rs.getString("status"));
				valuesFromDatabase.add(rowEntry);
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
	
	public static ArrayList<StudyingEntry> findStudyingCoursesByStudentID(String studentID) {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		StringBuilder query = new StringBuilder();
		int indexCounter = 1;

		query.append("select k.*, 'Läser' as Status from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudying.TABLENAME + " l on l." + DbStudying.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudying.PNUMBER);
		query.append(" where s." + DbStudent.PNUMBER + " = ?");
		queryFiller.put(indexCounter++, studentID);

		ArrayList<StudyingEntry> valuesFromDatabase = new ArrayList<StudyingEntry>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();

			while (rs.next()) {

				StudyingEntry rowEntry = new StudyingEntry(rs.getString(DbCourse.CODE.getDbName()),
						rs.getString(DbCourse.NAME.getDbName()), rs.getInt(DbCourse.POINTS.getDbName()));
				valuesFromDatabase.add(rowEntry);
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
	
	public static ArrayList<StudiedEntry> findStudiedCoursesByStudentID(String studentID) {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		StringBuilder query = new StringBuilder();
		int indexCounter = 1;

		query.append("select k.*, concat('Betyg ', l." + DbStudied.GRADE + ") as status from " + DbCourse.TABLENAME + " k ");
		query.append("inner join " + DbStudied.TABLENAME + " l on l." + DbStudied.CODE + " = k." + DbCourse.CODE);
		query.append(
				" inner join " + DbStudent.TABLENAME + " s on s." + DbStudent.PNUMBER + " = l." + DbStudied.PNUMBER);
		query.append(" where s." + DbStudent.PNUMBER + " = ?");
		queryFiller.put(indexCounter++, studentID);

		ArrayList<StudiedEntry> valuesFromDatabase = new ArrayList<StudiedEntry>();
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();

			while (rs.next()) {

				StudiedEntry rowEntry = new StudiedEntry(rs.getString(DbCourse.CODE.getDbName()),
						rs.getString(DbCourse.NAME.getDbName()), rs.getInt(DbCourse.POINTS.getDbName()),
						rs.getString("status"));
				valuesFromDatabase.add(rowEntry);
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
	
	public static boolean hasStudentStudiedOnCourse(String studentID, String courseID) {
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		
		String query = "select case when exists ( "
											+ "select * from " + DbStudied.TABLENAME 
											+ " where " + DbStudied.CODE + " = ? "
											+ "and " + DbStudied.PNUMBER + " = ? )"
					 + "then cast(1 as bit) "
					 + "else cast(0 as bit) end";
		queryFiller.put(1, courseID);
		queryFiller.put(2, studentID);
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		boolean alreadyExists = true;

		try {
			try {
				con = Connector.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();
			
			rs.next();
			alreadyExists = rs.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return alreadyExists;
		
	}
	
	public static boolean isStudentStudyingOnCourse(String studentID, String courseID) {
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		
		String query = "select case when exists ( "
											+ "select * from " + DbStudying.TABLENAME 
											+ " where " + DbStudying.CODE + " = ? "
											+ "and " + DbStudying.PNUMBER + " = ? )"
					 + "then cast(1 as bit) "
					 + "else cast(0 as bit) end";
		queryFiller.put(1, courseID);
		queryFiller.put(2, studentID);
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		boolean alreadyExists = true;

		try {
			try {
				con = Connector.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			rs = ps.executeQuery();
			
			rs.next();
			alreadyExists = rs.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
		return alreadyExists;
		
		
	}

	public static int addStudyingRelation(String studentNbr, ArrayList<String> courseCodes) throws SQLException {
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		
		int affectedRows = 0;
		
		StringBuilder query = new StringBuilder();
		query.append("insert into " + DbStudying.TABLENAME + " ("
				+ DbStudying.CODE + ", "
				+ DbStudying.PNUMBER + ") "
				+ "values ");
		
		int indexCounter = 1;
		boolean coursesAreToBeRemoved = courseCodes.size() > 0;
		for (String code : courseCodes) {
			query.append("(?, ?), ");
			queryFiller.put(indexCounter++, code);
			queryFiller.put(indexCounter++, studentNbr);
		}
		
		if (coursesAreToBeRemoved) query.setLength(query.length() - 2); // To get rid of ", "
		
		Connection con = null;
		PreparedStatement ps = null;

		try {
			try {
				con = Connector.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			affectedRows = ps.executeUpdate();
			
			return affectedRows;
			
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
		
	}

	public static int addStudiedRelation(String studentNbr, String courseCode, int grade) throws SQLException {
	
		
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		String query = "insert into " + DbStudied.TABLENAME + " ("
				+ DbStudied.CODE + ", "
				+ DbStudied.PNUMBER + ", "
				+ DbStudied.GRADE + ") "
				+ "values (?, ?, ?)";
		
		queryFiller.put(1, courseCode);
		queryFiller.put(2, studentNbr);
		queryFiller.put(3, grade);
		
		Connection con = null;
		PreparedStatement ps = null;

		try {
			try {
				con = Connector.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			return ps.executeUpdate();
			
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
	}
	
	public static int deleteStudent(String studentID) throws SQLException {

		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();

		String query = "delete from " + DbStudent.TABLENAME + " where " + DbStudent.PNUMBER+ " = ?";
		queryFiller.put(1, studentID);

		Connection con = null;
		PreparedStatement ps = null;

		try {
			try {
				con = Connector.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ps = con.prepareStatement(query);
			PsFiller.fill(ps, queryFiller);
			return ps.executeUpdate();
			
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}
	}

	public static int deRegisterStudentFromStudiedCourse(String studentID, String courseID) {
		
		String query = "delete from " + DbStudied.TABLENAME + " where " + DbStudied.PNUMBER + " = ? and "
				+ DbStudied.CODE + " = ?";
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		queryFiller.put(1, studentID);
		queryFiller.put(2, courseID);

		Connection con = null;
		PreparedStatement ps = null;

		int affectedRows = 0;
		
		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			
			affectedRows = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}

		return affectedRows;
	}
	
	public static int deRegisterStudentFromStudyingCourse(String studentID, String courseID) {

		String query = "delete from " + DbStudying.TABLENAME + " where " + DbStudying.PNUMBER + " = ? and "
				+ DbStudying.CODE + " = ?";
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		queryFiller.put(1, studentID);
		queryFiller.put(2, courseID);

		Connection con = null;
		PreparedStatement ps = null;

		int affectedRows = 0;
		
		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			
			affectedRows = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}

		return affectedRows;

	}
	
	public static int updateStudentGradeOnCourse(String studentID, String courseID, int grade) {
		
		String query = "update " + DbStudied.TABLENAME + " set " + DbStudied.GRADE + " = ? where " + DbStudied.PNUMBER + " = ? and "
				+ DbStudied.CODE + " = ?";
		HashMap<Integer, Object> queryFiller = new HashMap<Integer, Object>();
		queryFiller.put(1, grade);
		queryFiller.put(2, studentID);
		queryFiller.put(3, courseID);

		Connection con = null;
		PreparedStatement ps = null;

		int affectedRows = 0;
		
		try {
			con = Connector.getConnection();
			ps = con.prepareStatement(query.toString());
			PsFiller.fill(ps, queryFiller);
			
			affectedRows = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(con);
		}

		return affectedRows;
		
	}
}
