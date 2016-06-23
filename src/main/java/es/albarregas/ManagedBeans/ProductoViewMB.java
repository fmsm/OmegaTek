
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Producto;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class ProductoViewMB implements java.io.Serializable {
    
    private Producto prod;
    private boolean fav;

    public Producto getProd() {
        return prod;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
    
        
    /**
     * Dentro del constructor, obtengo el parametro id de la request, y obtengo la información de ese producto de la BD
     */
    public ProductoViewMB() {
        
        //obtener el parametro "id" de la request                
        String parametroID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        
        //controlar que el usuario no haya introducido directamente 'producto.xhtml' en la barra del direcciones del navegador, en cuyo caso parametroID 
        //sera null; y tb que, en caso de no ser null, este compuesto solo por números, es decir que no haya por ejemplo: 'producto.xhtml?id=1as2s'
        if (parametroID == null || !parametroID.matches("\\d+")) {            
            //redirigir a error 404 o a index
            redirigir404();            
        } else {

            //tambien hay que controlar que el usuario no haya introducido manualmente en la barra de direcciones del navegador, algo como:
            //producto.xhtml?id=xxxxxx, donde xxxxx sea una id que no exista en la BD 
                        
            //obtener info de ese producto de la base de datos
            DAOGenerico oDAO = new DAOGenerico();
                        
            String clausulaWhere = String.format("where idProducto = %s", parametroID);
            Iterator it = oDAO.leer("", "Producto", clausulaWhere, -1, -1).iterator();
            while (it.hasNext()) {
                prod = (Producto) it.next();
            }//while
            
            if (prod == null) {
                //significa que se introdujo en la barra de direcciones del navegador, una id manualmente y que no se corresponde con ningun producto de
                //la base de datos, por que redirigiremos a la pagina de error 404 o a index
                redirigir404();
            }//if prod == null
            
        }//if..else
        
        System.out.println("[INFO - MB] [ProductoViewMB] Constructor ejecutado"); 
                
    }//constructor
    
    
    public void redirigir404() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("resources/vistas/errores/404.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(IndexViewMB.class.getName()).log(Level.SEVERE, null, ex);
        }//try..catch
        FacesContext.getCurrentInstance().responseComplete();                         
    }
    
    public void comprar() {        
    }
    
    public void toggleFav() {
        this.fav = !this.fav;
    }
    
}//CLASS
