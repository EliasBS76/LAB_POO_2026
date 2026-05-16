package gym.service;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que gestiona el tema visual (oscuro / claro) de la aplicación.
 * Mantiene referencias a todas las escenas abiertas para aplicar el cambio
 * de forma global.
 */
public class ThemeService {

    private static ThemeService instance;

    private boolean dark = true;
    private final List<Scene> scenes = new ArrayList<>();

    private static final String CSS_DARK  = "/gym/styles.css";
    private static final String CSS_LIGHT = "/gym/styles-light.css";

    private ThemeService() {}

    public static ThemeService getInstance() {
        if (instance == null) instance = new ThemeService();
        return instance;
    }

    /** Registra una escena para que reciba los cambios de tema. */
    public void registerScene(Scene scene) {
        scenes.add(scene);
        applyTo(scene);
    }

    /** Alterna entre tema oscuro y claro en todas las escenas registradas. */
    public void toggle() {
        dark = !dark;
        scenes.forEach(this::applyTo);
    }

    private void applyTo(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
            getClass().getResource(dark ? CSS_DARK : CSS_LIGHT).toExternalForm()
        );
    }

    /** Retorna la URL del CSS activo (para diálogos secundarios). */
    public String getCurrentCss() {
        return getClass().getResource(dark ? CSS_DARK : CSS_LIGHT).toExternalForm();
    }

    public boolean isDark() { return dark; }
}
