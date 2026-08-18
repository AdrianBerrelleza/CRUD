package com.crud.GUi;

import com.crud.Logica.Persona;
import com.crud.Logica.Telefono;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PersonaFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtNuevoTelefono;
    @FXML private ListView<String> listTelefonos;

    private final ObservableList<String> telefonos = FXCollections.observableArrayList();
    private Persona persona;
    private boolean guardado = false;

    @FXML
    public void initialize() {
        listTelefonos.setItems(telefonos);
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
        txtNombre.setText(persona.getNombre());
        txtDireccion.setText(persona.getDireccion());
        telefonos.clear();
        persona.getTelefonos().forEach(t -> telefonos.add(t.getNumero()));
    }

    @FXML
    private void onAgregarTelefono() {
        String numero = txtNuevoTelefono.getText();
        if (numero != null && !numero.isBlank()) {
            telefonos.add(numero.trim());
            txtNuevoTelefono.clear();
        }
    }

    @FXML
    private void onQuitarTelefono() {
        String seleccionado = listTelefonos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            telefonos.remove(seleccionado);
        }
    }

    @FXML
    private void onGuardar() {
        if (txtNombre.getText() == null || txtNombre.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "El nombre es obligatorio.", ButtonType.OK).showAndWait();
            return;
        }

        persona.setNombre(txtNombre.getText().trim());
        persona.setDireccion(txtDireccion.getText() != null ? txtDireccion.getText().trim() : null);

        persona.getTelefonos().clear();
        for (String numero : telefonos) {
            Telefono telefono = new Telefono();
            telefono.setNumero(numero);
            persona.addTelefono(telefono);
        }

        guardado = true;
        cerrar();
    }

    @FXML
    private void onCancelar() {
        guardado = false;
        cerrar();
    }

    private void cerrar() {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }

    public Persona getPersona() {
        return persona;
    }

    public boolean isGuardado() {
        return guardado;
    }
}
