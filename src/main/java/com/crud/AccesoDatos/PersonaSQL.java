package com.crud.AccesoDatos;

import com.crud.Conexion.ConectDB;
import com.crud.Logica.Persona;

import java.sql.*;

public class PersonaSQL {

    public boolean insertarPersona(Persona persona) {
        String sql = "INSERT INTO Personas (nombre, direccion) VALUES (? , ?)";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDireccion());
            int filas = ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    persona.setId(id);
                }
            }
            System.out.println("Filas: " + filas);
            return filas >0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean borrarPersona(int id) {
        String sql = "DELETE FROM Personas WHERE id = ?";
        try(Connection conn = ConectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Filas: " + filas);
            return filas > 0;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
