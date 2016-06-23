
/**
 * Sacado de http://www.javaknowledge.info/authentication-based-secure-login-logout-using-jsf-2-0-and-primefaces-3-4-1/ 
 */

package es.albarregas.utils;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Usuario;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Francisco
 */
public class UtilSesion {
     
    public static String getSessionID(){
        return FacesContext.getCurrentInstance().getExternalContext().getSessionId(false);
    }
    
    public static HttpSession getSession() {
      return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }
    
    public static HttpSession getNewSession() {
      return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }    

//    public static HttpServletRequest getRequest() {
//     return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//    }
//
//    public static String getUserName() {
//      HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//      return  session.getAttribute("username").toString();
//    }
    
    //atributo de sesion que yo introduzco: nombre->userID tipo->int
    public static Integer getUserID() {
      HttpSession session = getSession();
      if ( session != null && session.getAttribute("userID") != null)
          return (int) session.getAttribute("userID");
      else
          return null;
    }
    
    //atributo de sesion que yo introduzco: nombre->nomUsuario tipo->String
    public static String getNomUsuario() {
      HttpSession session = getSession();
      if ( session != null && session.getAttribute("nomUsuario") != null)
          return (String) session.getAttribute("nomUsuario");
      else
          return null;
    }    
    
    /**
     * Metodo para obtener el usuario (objeto Usuario) correspondiente al usuario logeado actualmente.
     * @return objeto Usuario logeado actualmente o null si se produjo algun error
     */
    public static Usuario getUser() {
        
        Usuario user = null;
        DAOGenerico oDAO = new DAOGenerico();
        
        String clausulaWhere = String.format("where IdUsuario = %d", UtilSesion.getUserID());        
        List<Object> temporal = oDAO.leer("", "Usuario", clausulaWhere, -1, -1);
        Iterator it = temporal.iterator();
        
        while (it.hasNext()) {
            user = (Usuario) it.next();
        }//
        
        return user;        
    }//metodo
    
}//CLASS
