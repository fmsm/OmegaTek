
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Mensaje;
import es.albarregas.modelo.Producto;
import es.albarregas.modelo.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class AdminPnlViewMB implements java.io.Serializable {
    
    private LazyDataModel<Producto> modelProd;
    private LazyDataModel<Usuario> modelUsu;
    
    public LazyDataModel<Producto> getModelProd() {
        return modelProd;
    }

    public LazyDataModel<Usuario> getModelUsu() {
        return modelUsu;
    }
    
    private int destinoMsgID;
    private String asunto = "";
    private String mensajeStr = "";
        
    //atributo para ver el texto del mensaje cuando se hace click en el boton del sobre en la DataTable
    private Mensaje msgSeleccionado;
    
    private List<Mensaje> mensajesR;
    private List<Mensaje> mensajesE;

    public List<Mensaje> getMensajesR() {
        return mensajesR;
    }
    
    public List<Mensaje> getMensajesE() {
        return mensajesE;
    }    
            
    /**
     * Implementar los metodos 'load' de las DataTables de primefaces, que permiten 'lazy loading'.
     * Cargar en los atributos 'mensajesE' y 'mensajesR' los mensajes enviados y recibidos por el administrador.
     */
    @PostConstruct
    public void init() {
                
        //crear LazyDataModel para la tabla Producto, y sobreescribir el método load, para habilitar lazy loading para su DataTable correspondiente
        modelProd = new LazyDataModel<Producto>() {
            
            @Override
            public List<Producto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                
                DAOGenerico oDAO = new DAOGenerico();
                //averiguar numero total de productos
                long numTotalResultados = (long) oDAO.leer("count(*)", "Producto", "", -1, -1).iterator().next();                
                modelProd.setRowCount( (int) numTotalResultados );
                
                List<Object> temporal = oDAO.leer("", "Producto", "", first, pageSize);
                Iterator it = temporal.iterator();
                List<Producto> result = new ArrayList();                
                while (it.hasNext()) {
                    result.add( (Producto) it.next());                
                }//while                
                
                return result;
            }//load
            
        };//new LazyDataModel
        
        //crear LazyDataModel para la tabla Usuario, y sobreescribir el método load, para habilitar lazy loading para su DataTable correspondiente
        modelUsu = new LazyDataModel<Usuario>() {
                                    
            @Override
            public List<Usuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                
                DAOGenerico oDAO = new DAOGenerico();
                //averiguar numero total de usuarios/clientes (ya que es el mismo)
                long numTotalResultados = (long) oDAO.leer("count(*)", "Usuario", "", -1, -1).iterator().next();                        
                modelUsu.setRowCount( (int) numTotalResultados );
                
                List<Object> temporal = oDAO.leer("", "Usuario", "", first, pageSize);
                Iterator it = temporal.iterator();
                List<Usuario> result = new ArrayList();                
                while (it.hasNext()) {
                    result.add( (Usuario) it.next());                
                }//while                
                
                return result;
            }//load            
                        
        };//new LazyDataModel

        //cargar mensajes
        DAOGenerico oDAO = new DAOGenerico();
        
        //esto no seria siquiera necesario, pues ya se el id de usuario del administrador que es 1
        //Usuario admin = (Usuario) oDAO.leer("", "Usuario", "where IdUsuario = 1", -1, -1).iterator().next();
                
        mensajesE = oDAO.leerMensajesUsr(1, true);
        mensajesR = oDAO.leerMensajesUsr(1, false);
                
    }//init
    
    
    /**
     * Cambiar el estado de bloqueo del usuario, si esta bloqueado pasa a no estarlo y viceversa
     * @param u 
     */
    public void toggleBloqueoUsr(Usuario u) {
        
        DAOGenerico oDAO = new DAOGenerico();
                
        u.setBloqueado( (u.getBloqueado() == 's') ? 'n' : 's' ); 
        oDAO.crearOactualizar(u);
        
        String msgTxt = (u.getBloqueado() == 'n') ? "Usuario desbloqueado" : "Usuario bloqueado" ; 
        String nomCliente = u.getClienteUsuario().getNombre() + " " + u.getClienteUsuario().getApellidos();
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msgTxt, nomCliente);
        FacesContext.getCurrentInstance().addMessage(null, message);        
        
    }//metodo
    
    
    /**
     * Permite al administrador poner un producto en oferta o quitarlo
     * @param p 
     */
    public void toggleOfertaProd(Producto p) {
        
        DAOGenerico oDAO = new DAOGenerico();
                
        p.setOferta((p.getOferta() == 's') ? 'n' : 's' ); 
        oDAO.crearOactualizar(p);
        
        String msgTxt = (p.getOferta() == 'n') ? "Eliminada oferta en producto" : "Producto en oferta" ; 
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msgTxt, p.getDenominacion());
        FacesContext.getCurrentInstance().addMessage(null, message);        

    }//metodo
    
    
    /**
     * Permite al administrador establecer un producto como fuera de catalogo y tambien revertir el proceso
     * @param p 
     */
    public void toggleDescatProd(Producto p) {
        
        DAOGenerico oDAO = new DAOGenerico();
                
        p.setFueraCatalogo((p.getFueraCatalogo() == 's') ? 'n' : 's' ); 
        oDAO.crearOactualizar(p);
        
        String msgTxt = (p.getFueraCatalogo() == 'n') ? "Producto añadido al catalogo" : "Producto descatalogado" ; 
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msgTxt, p.getDenominacion());
        FacesContext.getCurrentInstance().addMessage(null, message);        
        
    }//metodo

            
    /**
     * Metodo para que el administrador envie un mensaje a un usuario registrado en la web.
     */
    public void enviarMsg() {
        
        //necesito: origen, destino, asunto, mensaje, fecha, leido ('s','n')
        //origen-> administrador
        //destino-> usuario registrado, tengo en un atributo su id, tengo que traerme el objeto usuario correspondiente si existe
        //lo demas es sencillo
        
        DAOGenerico oDAO = new DAOGenerico();
                
        //origen->administrador
        Usuario origen = null;
        List<Object> temporal = oDAO.leer("", "Usuario", "where IdUsuario = 1", -1, -1);
        Iterator it = temporal.iterator();

        while (it.hasNext()) {
            origen = (Usuario) it.next();
        }//while

        
        //destino
        Usuario destino = null;
        String clausulaWhere = String.format("where IdUsuario = %d", destinoMsgID);
        temporal = oDAO.leer("", "Usuario", clausulaWhere, -1, -1);
        it = temporal.iterator();
        
        while (it.hasNext()) {
            destino = (Usuario) it.next();
        }//while        
        
        //si el usuario que se ha introducido no existe, en este punto tendremos como destino = null
        //en tal caso, no debemos proceder a enviar el mensaje
        boolean ok = false;
        if (destino != null) {
            
            Mensaje mensaje = new Mensaje();
            mensaje.setOrigen(origen);
            mensaje.setDestino(destino);
            mensaje.setAsunto(asunto);
            mensaje.setMensaje(mensajeStr);
            mensaje.setFecha(new Date());
            mensaje.setLeido('n');

            ok = oDAO.crearOactualizar(mensaje);            
        }
                
        //reportar al usuario si se envio el mensaje correctamente o no, usando el booleano ok obtenido al intentar grabar el mensaje en la BD
        String mensajeG = (ok) ? "Mensaje enviado" : "Se produjo un error al enviar el mensaje. Intentelo de nuevo";
        FacesMessage message = new FacesMessage( (ok) ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, mensajeG, "");
        FacesContext.getCurrentInstance().addMessage(null, message);        
        
        asunto = "";
        mensajeStr = "";
        
    }//método
    
    
    /**
     * Mostrar dialogo con el texto del mensaje y marcar el mensaje como leido y actualizar a la base de datos, si procede
     * @param msg
     */
    public void leerMensaje(Mensaje msg) {
        
        DAOGenerico oDAO = new DAOGenerico();
        //ponemos en mensaje seleccionado el mensaje que se mostrara en el dialogo
        this.msgSeleccionado = msg;
        //vemos si el mensaje estaba como 'no leido' ('n'); y si es asi lo cambiamos a 's' y lo actualizamos en la BD
        if (msg.getLeido() == 'n') {
            msg.setLeido('s');
            oDAO.crearOactualizar(msg);
        } 
        //si estaba como 'leido' no hay que hacer nada
        
    }//leerMensaje    
    
    //<editor-fold defaultstate="collapsed" desc="constructor/getters/setters">    
    public AdminPnlViewMB() {
    }//constructor
    
    public int getDestinoMsgID() {
        return destinoMsgID;
    }

    public void setDestinoMsgID(int destinoMsgID) {
        this.destinoMsgID = destinoMsgID;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensajeStr() {
        return mensajeStr;
    }

    public void setMensajeStr(String mensajeStr) {
        this.mensajeStr = mensajeStr;
    }

    public Mensaje getMsgSeleccionado() {
        return msgSeleccionado;
    }

    public void setMsgSeleccionado(Mensaje msgSeleccionado) {
        this.msgSeleccionado = msgSeleccionado;
    }    
    //</editor-fold>    
    
}//CLASS
