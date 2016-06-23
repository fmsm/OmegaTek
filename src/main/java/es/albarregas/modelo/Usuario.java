
package es.albarregas.modelo;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.ColumnTransformer;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="usuarios")
public class Usuario implements java.io.Serializable {
        
    //con AI
    @Id
    @Column (name="IDUSUARIO")    
    private int idUsuario;
    
    //relacion uno a uno unidireccional
    @OneToOne(cascade = CascadeType.PERSIST)
    @PrimaryKeyJoinColumn
    private Cliente clienteUsuario;    
    
    @Column (name="EMAIL", unique=true)
    private String email;
    
    @Column (name="CLAVE")
    @ColumnTransformer(
       read="AES_DECRYPT(clave, SHA1('albarregas'))",
       write="AES_ENCRYPT(?, SHA1('albarregas'))")    
    private byte[] clave;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ULTIMOACCESO")    
    private Date ultimoAcceso;
    
    @Column (name="TIPO")
    private char tipo;
    
    @Column (name="BLOQUEADO")
    private char bloqueado;
    
    @Column (name="TOKEN")
    private String token;
    
    @OneToMany(mappedBy="origen")   //atributo "origen" de la clase java "mensaje"
    private Set<Mensaje> usuariosOrigen;    
            
    @OneToMany(mappedBy="destino")   //atributo "destino" de la clase java "mensaje"
    private Set<Mensaje> usuariosDestino;    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Usuario() {
    }

    public Usuario(Cliente clienteUsuario, String email, byte[] clave, Date ultimoAcceso, char tipo, char bloqueado) {
        this.clienteUsuario = clienteUsuario;
        this.email = email;
        this.clave = clave;
        this.ultimoAcceso = ultimoAcceso;
        this.tipo = tipo;
        this.bloqueado = bloqueado;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public int getIdUsuario() {
        return this.idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
       
    public byte[] getClave() {
        return this.clave;
    }
    
    public void setClave(byte[] clave) {
        this.clave = clave;
    }

    public Date getUltimoAcceso() {
        return this.ultimoAcceso;
    }
    
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
        
    public char getTipo() {
        return this.tipo;
    }
    
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }
        
    public char getBloqueado() {
        return this.bloqueado;
    }
    
    public void setBloqueado(char bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public Cliente getClienteUsuario() {
        return clienteUsuario;
    }

    public void setClienteUsuario(Cliente clienteUsuario) {
        this.clienteUsuario = clienteUsuario;
    }
                
    public Set<Mensaje> getUsuariosOrigen() {
        return usuariosOrigen;
    }

    public void setUsuariosOrigen(Set<Mensaje> usuariosOrigen) {
        this.usuariosOrigen = usuariosOrigen;
    }

    public Set<Mensaje> getUsuariosDestino() {
        return usuariosDestino;
    }

    public void setUsuariosDestino(Set<Mensaje> usuariosDestino) {
        this.usuariosDestino = usuariosDestino;
    }
    //</editor-fold>
   
    
    @Transient
    private String usrRole;
    @Transient
    private boolean usrBloqueado;

    public String getUsrRole() {
        return (this.tipo == 'a') ? "Administrador" : "Usuario";
    }

    public boolean isUsrBloqueado() {
        return (this.bloqueado == 's');
    }

    public void setUsrRole(String usrRole) {
        this.usrRole = usrRole;
    }

    public void setUsrBloqueado(boolean usrBloqueado) {
        this.usrBloqueado = usrBloqueado;
    }
    
}//CLASS


