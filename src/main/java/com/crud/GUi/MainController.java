package com.crud.GUi;

import com.crud.AccesoDatos.PersonaSQL;
import com.crud.AccesoDatos.TelefonoSQL;
import com.crud.Logica.Persona;
import com.crud.Logica.Telefono;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.stream.Collectors;

public class MainController {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, String> colDireccion;
    @FXML private TableColumn<Persona, String> colTelefonos;
    @FXML private Label lblEstado;

    // Se reutilizan tal cual las clases de acceso a datos ya existentes.
    private final PersonaSQL personaSQL = new PersonaSQL();
    private final TelefonoSQL telefonoSQL = new TelefonoSQL();

    private final ObservableList<Persona> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Persona no tiene un getter de teléfonos en texto, así que se arma
        // aquí mismo (en la GUI) a partir de getTelefonos().
        colTelefonos.setCellValueFactory(cellData -> {
            String texto = cellData.getValue().getTelefonos().stream()
                    .map(Telefono::getNumero)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(texto);
        });

        tablaPersonas.setItems(datos);
        refrescar();
    }

    @FXML
    private void onNuevo() {
        abrirFormulario(new Persona());
    }

    @FXML
    private void onEditar() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una persona de la tabla para editar.");
            return;
        }
        abrirFormulario(seleccionada);
    }

    @FXML
    private void onEliminar() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selecciona una persona de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar a " + seleccionada.getNombre() + " y sus teléfonos?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(boton -> {
            if (boton == ButtonType.YES) {
                // PersonaSQL.borrarPersona ya borra también sus teléfonos.
                personaSQL.borrarPersona(seleccionada.getId());
                refrescar();
                lblEstado.setText("Persona eliminada.");
            }
        });
    }

    @FXML
    private void onRefrescar() {
        refrescar();
    }

    private void abrirFormulario(Persona persona) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cruda/persona_form.fxml"));
            Parent root = loader.load();

            PersonaFormController controller = loader.getController();
            controller.setPersona(persona);

            Stage stage = new Stage();
            stage.setTitle(persona.getId() == 0 ? "Nueva persona" : "Editar persona");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isGuardado()) {
                guardarPersonaConTelefonos(controller.getPersona());
                refrescar();
                lblEstado.setText("Cambios guardados.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // <-- agrega esto
            mostrarAlerta("No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    /**
     * Orquesta el alta/modificación de una Persona junto con sus Telefonos
     * usando directamente PersonaSQL y TelefonoSQL (sin agregar ninguna
     * clase de lógica de negocio nueva).
     */
    private void guardarPersonaConTelefonos(Persona persona) {
        if (persona.getId() == 0) {
            personaSQL.insertarPersona(persona); // asigna el id generado
        } else {
            personaSQL.actualizarPersona(persona);
            telefonoSQL.borrarTelefono(persona.getId()); // se reemplazan los teléfonos
        }

        for (Telefono telefono : persona.getTelefonos()) {
            telefono.setPersonId(persona.getId());
            telefonoSQL.insertarTelefono(telefono);
        }
    }

    private void refrescar() {
//        try {
//            datos.setAll(personaSQL.obtenerTodasPersonas());
//            lblEstado.setText(datos.size() + " persona(s) cargada(s).");
//        } catch (Exception e) {
//            mostrarAlerta("No se pudo conectar a la base de datos: " + e.getMessage());
//        }
            try {
                datos.setAll(personaSQL.obtenerTodasPersonas());
                lblEstado.setText(datos.size() + " persona(s) cargada(s).");
            } catch (Exception e) {
                e.printStackTrace(); // <-- agrega esta línea
                mostrarAlerta("No se pudo conectar a la base de datos: " + e.getMessage());
            }

    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).showAndWait();
    }
}
