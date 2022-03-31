package org.ph.iwanttranseat.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.ph.iwanttranseat.java.model.BusModel;

public class BusDAO {
	
	private static final String INSERT_BUS_DATA = "INSERT INTO `iwanttranseat_db`.`bus` (bus_name, bus_type, bus_seats, available_seats, "
			+ "bus_number, plate_number, date_created, is_deleted) VALUES (?,?,?,?,?,?,?,?);";
	private static final String SELECT_BUS = "SELECT * FROM `iwanttranseat_db`.`bus` WHERE `is_deleted` <> 1;";
	private static final String VIEW_BUS = "SELECT * FROM `iwanttranseat_db`.`bus` WHERE `busId` = ?";
	private static final String UPDATE_BUS = "UPDATE `iwanttranseat_db`.`bus` SET `bus_name` = ?, `bus_type` = ?, `bus_seats` = ?, "
			+ "`available_seats` = ?, `bus_number` =?, `plate_number` =? WHERE `busId` = ?;";
	private static final String DELETE_BUS = "UPDATE `iwanttranseat_db`.`bus` SET `is_deleted` = ? WHERE (`busId` = ?);";

//	Adding BUS details:
	public void insertBus(BusModel busModel) {
		try (Connection connection = JDBCUtils.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BUS_DATA)) {
			preparedStatement.setString(1, busModel.getBusName());
			preparedStatement.setString(2, busModel.getBusType());
			preparedStatement.setString(3, busModel.getBusSeats());
			preparedStatement.setInt(4, busModel.getAvailableSeats());
			preparedStatement.setString(5, busModel.getBusNumber());
			preparedStatement.setString(6, busModel.getPlateNumber());
			preparedStatement.setDate(7,JDBCUtils.getSQLDate(busModel.getDateCreated()));
			preparedStatement.setBoolean(8, busModel.isDeleted());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Select Bus Details
	public BusModel selectBus(int id) {
		   BusModel busModel = null;
	        // Step 1: Establishing a Connection
	        try (Connection connection = JDBCUtils.getConnection();
	            // Step 2:Create a statement using connection object
	            PreparedStatement preparedStatement = connection.prepareStatement(VIEW_BUS);) {
	            preparedStatement.setInt(1, id);
	            System.out.println(preparedStatement);
	            // Step 3: Execute the query or update query
	            ResultSet rs = preparedStatement.executeQuery();
	            // Step 4: Process the ResultSet object.
	            while (rs.next()) {
	                int busId = rs.getInt("busId");
	                String busName = rs.getString("bus_name");
	                String busType = rs.getString("bus_type");
	                String busSeats = rs.getString("bus_seats");
	                int availableSeats = rs.getInt("available_seats");
	                String busNumber = rs.getString("bus_number");
	                String plateNumber = rs.getString("plate_number");
	                LocalDate dateCreated = rs.getDate("date_created").toLocalDate();
	                System.out.println(busId+" "+busName +" "+busType+" "+ busSeats+" "+busNumber+" "+plateNumber+" "+dateCreated);
	                busModel = new BusModel(busId, busName, busType, busSeats, availableSeats, busNumber, plateNumber, dateCreated);
	            }
	        } catch (SQLException exception) {
	        	JDBCUtils.printSQLException(exception);
	        }
	        return busModel;
	}
	
//	View BUS details:
	public List<BusModel> selectAllBus(){
		   List<BusModel> busList = new ArrayList<>();
			
		   try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BUS);) {
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int busId = rs.getInt("busId");
					String busName = rs.getString("bus_name");
					String busType = rs.getString("bus_type");
					String busSeats = rs.getString("bus_seats");
					int availableSeats = rs.getInt("available_seats");
					String busNumber = rs.getString("bus_number");
					String plateNumber = rs.getString("plate_number");
					LocalDate dateCreated = rs.getDate("date_created").toLocalDate();
					busList.add(new BusModel(busId, busName, busType, busSeats, availableSeats, busNumber, plateNumber, dateCreated));
				}
		   } catch (SQLException exception) {
			   JDBCUtils.printSQLException(exception);
		   }
		   return busList;
	}
	
//	Update Bus Details
	public boolean updateBusDetails(BusModel busModel) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_BUS);) {
					statement.setString(1, busModel.getBusName());
					statement.setString(2, busModel.getBusType());
					statement.setString(3, busModel.getBusSeats());
					statement.setInt(4, busModel.getAvailableSeats());
					statement.setString(5, busModel.getBusNumber());
					statement.setString(6, busModel.getPlateNumber());
					statement.setInt(7, busModel.getBusId());
					rowUpdated = statement.executeUpdate() > 0;
				}
				return rowUpdated;
	}
	
//	Delete Bus Details
	public boolean deleteBusDetails(BusModel busModel) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_BUS);) {
					statement.setBoolean(1, busModel.isDeleted());
					statement.setInt(2, busModel.getBusId());
					rowUpdated = statement.executeUpdate() > 0;
				}
				return rowUpdated;
	}
	
	
	// populate "select bus"
	private static final String SELECT_TRAVELBUS = "SELECT busId, CONCAT(bus_name, ' | ', bus_type, ' | Capacity:', bus_seats, ' seats | available_seats | Plate no.: ', plate_number) "
			+ "AS travel_bus FROM bus WHERE is_deleted <> 1 ORDER BY busId;";

	public static List<BusModel> selectTravelBus() {
		List<BusModel> travelBus = new ArrayList<>();
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRAVELBUS)) {
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int busId = rs.getInt("busId");
				String bus = rs.getString("travel_bus");
				System.out.println(bus);
				travelBus.add(new BusModel(busId, bus));

			}
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
		return travelBus;
	}
}
