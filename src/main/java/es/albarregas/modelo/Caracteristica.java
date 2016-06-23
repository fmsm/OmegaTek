
package es.albarregas.modelo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="caracteristicas")
public class Caracteristica implements java.io.Serializable{
        
    //con AI (esta tabla no viene definida en el fichero .pdf)
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona        
    @Id
    @Column(name="IDCARACTERISTICA")
    private short idCaracteristica;
        
    @ManyToOne
    @JoinColumn(name="IDCATEGORIA")    
    private Categoria categoria;    
    
    @Column(name="NOMBRE")
    private String nombre;    
        
    @OneToMany(mappedBy = "producto")   //atributo "producto" de la clase java "Caracyprod"
    private Set<Caracyprod> productos = new HashSet();    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Caracteristica() {
    }

    public Caracteristica(Categoria categoria, String nombre) {
        this.categoria = categoria;
        this.nombre = nombre;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public short getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(short idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Caracyprod> getProductos() {
        return productos;
    }

    public void setProductos(Set<Caracyprod> productos) {
        this.productos = productos;
    }
    //</editor-fold>    

}//CLASS
