package comun;

import java.io.Serializable;

/**
 * Created by estudiante on 29/03/17.
 */

public class AutoId implements Serializable {
    private String contenido;

    public AutoId(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}
