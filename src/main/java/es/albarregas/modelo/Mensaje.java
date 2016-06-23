
package es.albarregas.modelo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="mensajes")
public class Mensaje implements java.io.Serializable {
    
    //con AI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name="IDMENSAJE")
    private int idMensaje;
    
    @ManyToOne
    @JoinColumn(name="ORIGEN")
    private Usuario origen;
    
    @ManyToOne
    @JoinColumn(name="DESTINO")
    private Usuario destino;    
    
    @Column(name="ASUNTO")
    private String asunto;
    
    @Column(name="MENSAJE", columnDefinition = "TEXT")    
    private String mensaje;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA")        
    private Date fecha;
    
    @Column(name="LEIDO")
    private char leido;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Mensaje() {
    }

    public Mensaje(Usuario origen, Usuario destino, String asunto, String mensaje, Date fecha, char leido) {
        this.origen = origen;
        this.destino = destino;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leido = leido;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Usuario getOrigen() {
        return origen;
    }

    public void setOrigen(Usuario origen) {
        this.origen = origen;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public char getLeido() {
        return leido;
    }

    public void setLeido(char leido) {
        this.leido = leido;
    }
    //</editor-fold>
    
    @Transient
    private boolean msgLeido;

    public boolean isMsgLeido() {
        return (this.leido == 's');
    }

    public void setMsgLeido(boolean msgLeido) {
        this.msgLeido = msgLeido;
    }
    
    
        
}//CLASS
