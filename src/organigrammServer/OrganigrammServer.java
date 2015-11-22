package organigrammServer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.swing.JOptionPane;
import javax.xml.ws.Endpoint;

@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT)
public class OrganigrammServer {

	public OrganigrammServer(boolean createTables) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			System.out.println("Connection established");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return;
		}
		try {
			if (!createTables) {
				DatabaseMetaData dbmd = conn.getMetaData();
				ResultSet rs = dbmd.getTables(null, null, "DEPARTMENT", new String[] {"TABLE"});
				//				    while (rs.next()) {
				//				        System.out.println(rs.getString("TABLE_NAME"));
				//				            System.out.println(rs.getString("TABLE_SCHEM"));
				//				    }
				if (rs.next())
					return;
			}
			stmt = conn.createStatement();
			try {stmt.executeUpdate("DROP TABLE Department"); System.out.println("Department Table dropped");} catch(Exception e) { }
			try {stmt.executeUpdate("DROP TABLE Employee"); System.out.println("Employee Table dropped");} catch(Exception e) { }
			String ct = "CREATE TABLE Department (" +
					"Id       integer generated always as identity," +
					"Name     VARCHAR(100)," +
					"BossName VARCHAR(100)," +
					"HeadId   INTEGER," +
					"PRIMARY  KEY(Id))";
			stmt.executeUpdate(ct);
			String insert = "INSERT INTO Department (Name, BossName, HeadId) VALUES('EntertainmentComics', 'Walt d''Isney', 0)";
			stmt.executeUpdate(insert);
			System.out.println("Department Table created");
			ct = "CREATE TABLE Employee (" +
					"Id         integer generated always as identity," +
					"Name       VARCHAR(100)," +
					"Email      VARCHAR(100)," +
					"PersNummer VARCHAR(100)," +
					"DeptId     INTEGER, " +
					"PRIMARY KEY(Id))";
			stmt.executeUpdate(ct);
			System.out.println("Employee Table created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return;
			}
		}
	}

	@WebMethod
	public ArrayList<Abteilung> getAbteilungsListe(int headId) {
		ArrayList<Abteilung> al = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Department WHERE HeadId = " + headId);
			while(rs.next()) {
				al.add(new Abteilung(rs.getInt("Id"), rs.getString("Name"), rs.getString("BossName"), rs.getInt("HeadId")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return al;	
	}

	@WebMethod
	public ArrayList<Integer> getMitarbeiterIdListe(int abteilungId) {
		ArrayList<Integer> ml = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Employee WHERE DeptId = " + abteilungId);
			while(rs.next()) {
				ml.add(new Integer(rs.getInt("Id")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return ml;	
	}

	@WebMethod
	public Mitarbeiter getMitarbeiterDetails(int id) {
		Mitarbeiter m = new Mitarbeiter();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Employee WHERE Id = " + id);
			if (rs.next()) {
				m.setId(rs.getInt("Id"));
				m.setName(rs.getString("Name"));
				m.seteMail(rs.getString("Email"));
				m.setPersNr(rs.getString("PersNummer"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return m;
	}

	@WebMethod
	public Mitarbeiter addMitarbeiter(Mitarbeiter m) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			String insert = "INSERT INTO Employee (Name, Email, PersNummer, DeptId) VALUES('" +
					m.getName() + "', '" + m.geteMail() + "', '" + m.getPersNr() + "', " + m.getDeptId() +  ")";
			stmt.executeUpdate(insert);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return m;
	}

	@WebMethod
	public boolean deleteMitarbeiter(int id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			String delete = "DELETE FROM Employee WHERE Id = " + id;
			stmt.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}

	@WebMethod
	public boolean updateMitarbeiter(Mitarbeiter m) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			String update = "UPDATE Employee SET NAME = '" + m.getName() + "', Email = '" + m.geteMail() + "', PersNummer = '" + m.getPersNr() + "', DeptId =  " + m.getDeptId() + " WHERE Id = " + m.getId();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}

	@WebMethod
	public Abteilung addAbteilung(Abteilung a) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;create=true");
			stmt = conn.createStatement();
			String insert = "INSERT INTO Department (Name, BossName, HeadId) VALUES('" + a.getName() + "', '" + a.getBoss() + "', " + a.getHeadId() + ")";
			stmt.executeUpdate(insert);
			String identitiy = "SELECT IDENTITY_VAL_LOCAL() FROM Department";
			ResultSet rs = stmt.executeQuery(identitiy);
			rs.next();
			int id = rs.getInt("1");
			a.setId(id);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return a;
	}

	@WebMethod
	public boolean deleteAbteilung(int id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;");
			stmt = conn.createStatement();
			String delete = "DELETE FROM Department WHERE Id = " + id;
			stmt.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}

	@WebMethod
	public boolean updateAbteilung(Abteilung a) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:derby:c:\\users\\hermann\\workspace\\work.db;");
			stmt = conn.createStatement();
			String update = "UPDATE Department SET NAME = '" + a.getName() + "', BossName = '" + a.getBoss() + "', DeptId = " + a.getHeadId() +  " WHERE Id = " + a.getId();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void main(String[] args) {
		OrganigrammServer os = new OrganigrammServer(false);
		Endpoint endpoint = Endpoint.publish("http://localhost:9090/OrganigrammServices", os);
		JOptionPane.showMessageDialog(null, "OrganigrammServer beenden");
		endpoint.stop();
	}

}
