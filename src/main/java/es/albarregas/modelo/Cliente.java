
package es.albarregas.modelo;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="clientes")
public class Cliente implements java.io.Serializable {
    
    //sin AI
    @Id    
    @Column (name="IDCLIENTE")    
    private int idCliente;
        
    @Column (name="NOMBRE")
    private String nombre;
    
    @Column (name="APELLIDOS")
    private String apellidos;
    
    @Column (name="NIF")
    private String nif;

    @Temporal(TemporalType.DATE)
    @Column(name="FECHANACIMIENTO")    
    private Date fechaNacimiento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAALTA")    
    private Date fechaAlta;

    @Column (name="SEXO")
    private char sexo;        
    
    @OneToMany(mappedBy="cliente")    //atributo "cliente" de la clase "Direccion" (Direccion.cliente)
    private Set<Direccion> direcciones;
                                
    @OneToMany(mappedBy="cliente")    //atributo "cliente" de la clase "Pedido" (Pedido.cliente) / Tabla PEDIDOS
    private Set<Pedido> pedidos;

    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Cliente() {
    }

    public Cliente(int idCliente, String nombre, String apellidos, String nif, Date fechaNacimiento, Date fechaAlta) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaAlta = fechaAlta;
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public int getIdCliente() {
        return this.idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        
    public String getApellidos() {
        return this.apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
        
    public String getNif() {
        return this.nif;
    }
    
    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getFechaNacimiento() {
        return this.fechaNacimiento;
    }
    
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaAlta() {
        return this.fechaAlta;
    }
    
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    
    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    
    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Set<Direccion> direcciones) {
        this.direcciones = direcciones;
    }    
    
    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }            
    //</editor-fold>

}//CLASS


