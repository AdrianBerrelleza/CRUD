package com.crud.AccesoDatos;

import com.crud.Conexion.ConectDB;
import com.crud.Logica.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaSQL {

    private final TelefonoSQL telefonoSQL = new TelefonoSQL();

    public boolean insertarPersona(Persona persona) {
        String sql = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDireccion());
            int filas = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    persona.setId(rs.getInt(1));
                }
            }
            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar persona", e);
        }
    }

    public boolean actualizarPersona(Persona persona) {
        String sql = "UPDATE Personas SET nombre = ?, direccion = ? WHERE id = ?";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getDireccion());
            ps.setInt(3, persona.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar persona", e);
        }
    }

    public Persona obtenerPersona(int id) {
        String sql = "SELECT * FROM Personas WHERE id = ?";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Persona persona = new Persona();
                    persona.setId(rs.getInt("id"));
                    persona.setNombre(rs.getString("nombre"));
                    persona.setDireccion(rs.getString("direccion"));

                    // Cargar los teléfonos asociados
                    telefonoSQL.obtenerTelefonosIdPersona(id)
                            .forEach(persona::addTelefono);

                    return persona;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener persona con id " + id, e);
        }
        return null; // no encontrada
    }

    public boolean borrarPersona(int id) {
        String sql = "DELETE FROM Personas WHERE id = ?";
        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Primero borramos los teléfonos para no dejar registros huérfanos
            telefonoSQL.borrarTelefono(id);

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar persona con id " + id, e);
        }
    }


    public List<Persona> obtenerTodasPersonas() {
        String sql = "SELECT * FROM Personas";
        List<Persona> personas = new ArrayList<>();

        try (Connection conn = ConectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Persona persona = new Persona();
                persona.setId(rs.getInt("id"));
                persona.setNombre(rs.getString("nombre"));
                persona.setDireccion(rs.getString("direccion"));

                telefonoSQL.obtenerTelefonosIdPersona(persona.getId())
                        .forEach(persona::addTelefono);

                personas.add(persona);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las personas", e);
        }
        return personas;
    }
}
