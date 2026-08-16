package com.crud.AccesoDatos;

import com.crud.Conexion.ConectDB;
import com.crud.Logica.Telefono;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TelefonoSQL {

    public boolean insertarTelefono(Telefono telefono) {
        String sql = "INSERT INTO telefonos (personaId, telefono) VALUES (?, ?)";
        try(Connection conn = ConectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, telefono.getPersonId());
            ps.setString(2, telefono.getNumero());

            return ps.executeUpdate() > 0;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Telefono> obtenerTelefonosIdPersona(int personaId) {
        String sql = "SELECT * FROM telefonos WHERE personaId = ?";
        List<Telefono> telefonos = new ArrayList<>();

        try(Connection conn = ConectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, personaId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Telefono tl = new Telefono();
                    tl.setId(rs.getInt("id"));
                    tl.setNumero(rs.getString("telefono"));
                    telefonos.add(tl);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();

        }
        return telefonos;
    }

    public boolean borrarTelefono(int personaId) {
        String sql = "DELETE FROM telefonos WHERE personaId = ?";
        try(Connection conn = ConectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, personaId);

            return ps.executeUpdate() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
