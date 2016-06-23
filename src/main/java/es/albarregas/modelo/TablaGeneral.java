
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="general")
public class TablaGeneral implements java.io.Serializable {
    
    //la tabla GENERAL no tiene PK, pero hibernate parece no dejar mapear una tabla sin ponerle un @Id
    @Id
    @Column(name="GASTOSENVIO")
    private float gastosEnvio;
    
    @Column(name="IVA")
    private float iva;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public TablaGeneral() {
    }

    public TablaGeneral(float gastosEnvio, float iva) {
        this.gastosEnvio = gastosEnvio;
        this.iva = iva;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public float getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(float gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }
    //</editor-fold>    
        
}//CLASS
