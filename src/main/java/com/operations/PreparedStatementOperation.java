package com.operations;

import com.util.DBConnection;
import com.util.DBRow;

import java.sql.*;
import java.util.ArrayList;

public class PreparedStatementOperation {

	private Connection cxn = null;

	private static final String SELECT_ALL = "SELECT * FROM vertx.horoscope";
	private static final String SELECT_ROWS_byID = "SELECT * FROM vertx.horoscope WHERE id = ?";
	private static final String SELECT_ROWS_byYEAR = "SELECT * FROM vertx.horoscope WHERE year = ?";
	private static final String SELECT_ROWS_bySIGN = "SELECT * FROM vertx.horoscope WHERE sign = ?";
	private static final String SELECT_ROWS_byMONTH = "SELECT * FROM vertx.horoscope WHERE month = ?";
	private static final String SELECT_ROWS_byDAY_SCORE = "SELECT * FROM vertx.horoscope WHERE day_score = ?";

	private static final String INSERT_ROW = "INSERT vertx.horoscope(year,sign,month,day_score) VALUES (?,?,?,?)";

	//Constructor
	public PreparedStatementOperation() {

		String msg = "";

		try {
			cxn = DBConnection.getConnection();
			System.out.println("PSO connection success");
		} catch (SQLException ex) {
				ex.printStackTrace();
		}

	}

	public ArrayList<DBRow> selectRows(String filterColumn, String filterValue) { //Select all rows if input parameters are ""
		ArrayList<DBRow> rows = new ArrayList<DBRow>();

		try {
			if (cxn != null) {

				PreparedStatement pstmt;

				switch (filterColumn) {
					case "id":
						pstmt = cxn.prepareStatement(SELECT_ROWS_byID);
						break;
					case "year":
						pstmt = cxn.prepareStatement(SELECT_ROWS_byYEAR);
						break;
					case "sign":
						pstmt = cxn.prepareStatement(SELECT_ROWS_bySIGN);
						break;
					case "month":
						pstmt = cxn.prepareStatement(SELECT_ROWS_byMONTH);
						break;
					case "day_score":
						pstmt = cxn.prepareStatement(SELECT_ROWS_byDAY_SCORE);
						break;
					default:
						pstmt = cxn.prepareStatement(SELECT_ALL);
				}

				if (!filterColumn.equals("")) {
					if (filterColumn.equals("id") || filterColumn.equals("year"))
						pstmt.setInt(1, Integer.parseInt(filterValue));
					else
						pstmt.setString(1, filterValue);
				}

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
						//int id = rs.getInt("id");
						rows.add(new DBRow(
							rs.getInt("id"),
							rs.getInt("year"),
							rs.getString("sign"),
							rs.getString("month"),
							rs.getString("day_score")
							));
				}
			}
		} catch (SQLException ex) {
				ex.printStackTrace();
		}
		return rows;
	}//selectRows

	public String insertRow(int year, String sign, String month, String day_score) {

		String msg = "";

		try {
			if (cxn != null) {
					PreparedStatement pstmt = cxn.prepareStatement(INSERT_ROW);
					pstmt.setInt(1, year);
					pstmt.setString(2, sign);
					pstmt.setString(3, month);
					pstmt.setString(4, day_score);
					int insertOutput = pstmt.executeUpdate();
					msg = insertOutput == 1 ? "INSERT SUCCESS" : "INSERT ERROR";
			}
		} catch (SQLException ex) {
				msg = "INSERT SQL EXCEPTION: " + ex.toString();
		}

		return msg;
	}//insertRow()

}//PreparedStatementOperation
