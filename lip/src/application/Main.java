package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bancoDados.DB;

public class Main {

	public static void main(String[] args) {

		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = DB.getConnection();

//			st = conn.prepareStatement("DELETE FROM departamento " + "WHERE " + "id = ?");
//
//			st.setInt(1, 2);
//
//			int registros = st.executeUpdate();
//
//			System.out.println("Finalizado! Registros = " + registros);

		} catch (Exception e) {
			System.out.println(e.fillInStackTrace());
			// throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}