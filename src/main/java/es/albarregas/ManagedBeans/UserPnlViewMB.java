
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Mensaje;
import es.albarregas.modelo.Usuario;
import es.albarregas.utils.UtilSesion;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class UserPnlViewMB implements java.io.Serializable {

    private String destinoMsgStr = "Administrador";
    private String asunto = "";
    private String mensajeStr = "";
    
    //tomara un valor dependiendo del menuitem de la vista 'contenidoUser' que el usuario haya pulsado en ultimo lugar
    //indicara que accion quiere realizar el usuario y se usara en las renderizaciones parciales de la vista via Ajax
    //0->enviar 1-> mens.recibidos 2->mens.enviados 3-> 4-> 5->
    private int opcionSeleccionada;
    
    //atributo para ver el texto del mensaje cuando se hace click en el boton del sobre en la DataTable
    private Mensaje msgSeleccionado;
    
    private List<Mensaje> mensajes;

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    
    /**
     * Cargar en el atributo 'mensajes' los mensajes recibidos o enviados por el usuario logeado en esta sesion
     * @param enviados Selecciona si se obtendran los mensajes enviados o los recibidos
     */    
    public void cargarMensajes(boolean enviados) {
        
        DAOGenerico oDAO = new DAOGenerico();
        
        Usuario usr = UtilSesion.getUser();
                
        mensajes = oDAO.leerMensajesUsr(usr.getIdUsuario(), enviados);
        
        opcionSeleccionada = enviados ? 2 : 1;
        
    }//
    
    /**
     * Enviar un mensaje del usuario que se encuentra logeado, a otro usuario o al administrador.
     * (DE MOMENTO TENGO PUESTO POR DEFECTO QUE LO MANDA AL ADMINISTRADOR)
     */
    public void enviarMsg() {
        
        //necesito: origen, destino, asunto, mensaje, fecha, leido ('s','n')
        //origen-> obtener Usuario a partir de atributo de sesion
        //destino-> por ahora va a estar limitado al administrador, asi que obtener usuario con id = 1
        //lo demas es sencillo
        
        DAOGenerico oDAO = new DAOGenerico();
        
        //origen
        Usuario origen = UtilSesion.getUser();
        
        //destino
        Usuario destino = null;
        List<Object> temporal = oDAO.leer("", "Usuario", "where IdUsuario = 1", -1, -1);
        Iterator it = temporal.iterator();
        
        while (it.hasNext()) {
            destino = (Usuario) it.next();
        }//while
        
        Mensaje mensaje = new Mensaje();
        mensaje.setOrigen(origen);
        mensaje.setDestino(destino);
        mensaje.setAsunto(asunto);
        mensaje.setMensaje(mensajeStr);
        mensaje.setFecha(new Date());
        mensaje.setLeido('n');
        
        boolean ok = oDAO.crearOactualizar(mensaje);
        
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
    /**
     * Constructor
     */
    public UserPnlViewMB() {
    }

    public String getDestinoMsgStr() {
        return destinoMsgStr;
    }

    public void setDestinoMsgStr(String destinoMsgStr) {
        this.destinoMsgStr = destinoMsgStr;
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
    
    public void setOpcionSeleccionada(int opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public int getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public Mensaje getMsgSeleccionado() {
        return msgSeleccionado;
    }

    public void setMsgSeleccionado(Mensaje msgSeleccionado) {
        this.msgSeleccionado = msgSeleccionado;
    }
    //</editor-fold>
    
}//CLASS
