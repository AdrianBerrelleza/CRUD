package com.crud.AccesoDatos;

import com.crud.Conexion.ConectDB;
import com.crud.Logica.Telefono;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefonoSQL {

    public boolean insertarTelefono(Telefono telefono) {
        String sql = "INSERT INTO telefonos (personaId, telefono) VALUES (?, ?)";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, telefono.getPersonId());
            ps.setString(2, telefono.getNumero());

            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    telefono.setId(rs.getInt(1));
                }
            }
            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar telefono", e);
        }
    }

    public List<Telefono> obtenerTelefonosIdPersona(int personaId) {
        String sql = "SELECT * FROM telefonos WHERE personaId = ?";
        List<Telefono> telefonos = new ArrayList<>();

        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Telefono tl = new Telefono();
                    tl.setId(rs.getInt("id"));
                    tl.setNumero(rs.getString("telefono"));
                    tl.setPersonId(rs.getInt("personaId")); // <-- antes faltaba esta línea
                    telefonos.add(tl);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener telefonos de persona " + personaId, e);
        }
        return telefonos;
    }

    public boolean borrarTelefono(int personaId) {
        String sql = "DELETE FROM telefonos WHERE personaId = ?";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, personaId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar telefonos de persona " + personaId, e);
        }
    }
}
