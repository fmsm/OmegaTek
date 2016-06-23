
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Cliente;
import es.albarregas.modelo.Usuario;
import es.albarregas.utils.UtilSesion;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class AccesoViewMB implements Serializable {
                   
    //atributos usados para el login
    private String email = "";
    private String password = "";
    private boolean recordarme = false;
    
    //atributos usados para el registro
    private String nomReg = "";
    private String apeReg = "";
    private String passwordMatch = "";
    private Date fecNacReg = null; 
    private String nifReg = "";
    
                        
    /**
     * Método en el que se realiza el proceso de comprobación de la identificación del ususario. Bien a traves de las cookies o a traves
     * de la información proporcionada por el usuario en el dialogo correspondiente
     * @param event 
     */
    public void login(ActionEvent event) {
        
        FacesMessage message;
        String nomUsuario = "";         //nombre y 1er apellido, que se mostrará cuando el usuario este logeado                  
        boolean loginCorrecto;
        boolean adminLoginCorrecto;
        
        DAOGenerico oDAO = new DAOGenerico();        
        
        //comprobar credenciales: email y contraseña, introducidas por el usuario
        Usuario u = oDAO.login(email, password.getBytes(StandardCharsets.UTF_8));
        
        //el usuario que se ha logeado es el administrador??
        adminLoginCorrecto = (u != null && u.getTipo() == 'a');
        
        //cambiar el valor que indica se ha logeado el usuario correctamente o no
        loginCorrecto = (u != null);
        
        //comprobar que el usuario no este bloqueado
        loginCorrecto = (u != null && u.getBloqueado() == 's') ? false : loginCorrecto;
                                        
        //si el login ha sido correcto, hay que hacer varias cosas (pero siempre que no sea el administrador)
        if (loginCorrecto && !adminLoginCorrecto) {            
            posLogin(u);            
        }//if loginCorrecto
            
        //se ha logeado el administrador??
        if (adminLoginCorrecto) {
            try {
                //ir a la pagina del panel de administración
                FacesContext.getCurrentInstance().getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AccesoViewMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //continuar proceso normal 
            
            //mensaje que se mostrará en el growl
            String strMessage;

            if (u != null && u.getBloqueado() == 's') {
                strMessage = "Usuario Bloqueado";
            } else {
                if (loginCorrecto) {
                    strMessage = "¡Bienvenido/a!";
                } else {
                    strMessage = "Email y/o contraseña incorrectos";
                }//if loginCorrecto else
            }//bloqueado

            message = (loginCorrecto) ? new FacesMessage(FacesMessage.SEVERITY_INFO, strMessage, nomUsuario) 
                                      : new FacesMessage(FacesMessage.SEVERITY_WARN, strMessage, "");        

            FacesContext.getCurrentInstance().addMessage(null, message);
            //esto es para pasarle un parámetro a la función javascript que hace que el dialog de login se oculta (login ok) o tiemble (login fallido)
            RequestContext.getCurrentInstance().addCallbackParam("loginCorrecto", loginCorrecto);                                                                     
            
        }//if (adminLoginCorrecto) else
        
    }//metodo    

    
    /**
     * Método al que llamamos cuando se ha hecho login correctamente y en el que se deben realizar una serie de operaciones, descritas dentro del metodo 
     * @param u
     */
    public void posLogin(Usuario u) {
        
        DAOGenerico oDAO = new DAOGenerico();

        //iniciar una nueva sesion con boolean a true
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
        //y añadir un atributo para identificar al usuario logeado;         
        session.setAttribute("userID", u.getIdUsuario());

        //y otro para almacenar su nombre y 1er apellido (esto utlimo usado en la barra de navegación)        
        //pero esto ultimo, tengo que leerlo de la tabla clientes
        String query = "Cliente where idCliente = '" + u.getIdUsuario() + "'";
        Cliente c = (Cliente) oDAO.leer(query).iterator().next();
        String nomUsuario = String.format("%s %s", c.getNombre(), c.getApellidos().split(" ")[0]);
        session.setAttribute("nomUsuario", nomUsuario);
            
        //tambien hay que actualizar en la base de datos el valor de Usuario.ultimoAcceso (el timestamp que indica el ultimo login en la web)        
        u.setUltimoAcceso(null);    //pongo el valor de .ultimoAcceso a null, para que asi se genere solo al hacer el update
        oDAO.crearOactualizar(u);   //se hace el update y el valor queda actualizado en la BD

        //fue activada la opcion "recordarme" en el dialog de login?? (si se llego a pasar por alli)
        if (recordarme) {
            //si --> generar token de usuario que servira para identificar al ususario
            generarUUID(u);
        }//if

        //limpiar contenidos de estos atributos
        email = "";
        password = "";
        recordarme = false;            
        
    }//postLogin
    
    
    /**
     * Método en el que se realiza el proceso de registro de un nuevo usuario. Si el registro se lleva a cabo con exito, se le logea automaticamente y
     * se guardan cookies en el sistema.
     */
    public void registro() {
        
        String nomUsuario;         //nombre y 1er apellido, que se mostrará cuando el usuario este logeado        
        boolean resultado;
        FacesMessage message;        
        boolean loginCorrecto = false;        
        Usuario usuario = new Usuario();
        Cliente cliente = new Cliente();
        DAOGenerico oDAO = new DAOGenerico();
        RequestContext context = RequestContext.getCurrentInstance();
        
        //establecer valores del objeto usuario y guardarlo en la base de datos
        usuario.setEmail(email);
        usuario.setClave(password.getBytes(StandardCharsets.UTF_8));
        usuario.setTipo('u');
        usuario.setBloqueado('n');
        usuario.setToken(null);
                        
        resultado = oDAO.crearOactualizar(usuario);
        
        //¿se produjo algun error en la operacion de escritura en la DB?
        if (resultado) {
            //no --> seguir el proceso
            //obtener de la BD, del campo 'idUsuario' del usuario que acabamos de guardar, para asignarselo como 'idCliente' al objeto cliente que se creara
            //a continuación
            String clausulaWhere = "where email = '" + email + "'";
            List<Object> temporal = oDAO.leer("idUsuario", "Usuario", clausulaWhere, 0, 1);
            Iterator it = temporal.iterator();
            int idUsuario = 0;
            while (it.hasNext()) {
                idUsuario = (Integer) it.next();
            }
            
            //establecer valores del objeto cliente y guardarlo en la base de datos
            cliente.setIdCliente(idUsuario);
            cliente.setNombre(nomReg);
            cliente.setApellidos(apeReg);
            cliente.setNif(nifReg);
            cliente.setSexo('m');       //DE MOMENTO ASI PARA QUE NO PETE, YA LO IMPLEMENTARE BIEN
            if (fecNacReg != null) {    //fecha nacimiento: opcional
                cliente.setFechaNacimiento(fecNacReg);  
            }//if            
            cliente.setFechaAlta(new Date());                    

            resultado = oDAO.crearOactualizar(cliente);
            
            message = (resultado) ? new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro completado con éxito", "") 
                                  : new FacesMessage(FacesMessage.SEVERITY_WARN, "Se produjo un error inesperado. Intentelo de nuevo", "");
            
            loginCorrecto = resultado;
            //y si todo ha ido bien, al igual que cuando el usuario 'inicia sesion' tenemos que obtener mostrar el nombre y 1er apellido del usuario
            //para asi actaulizar el valor del atributo creado para ello
            if (loginCorrecto) {
                
                nomUsuario = String.format("%s %s",nomReg,apeReg.split(" ")[0]);
                                                
                //y al igual que cuando hacemos login, tras hacer registro el usuario pasará a estar logeado automaticamente en la página
                //porlo que tambien hay que iniciar una nueva sesion con boolean a true y añadirle un atributo
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute("userID", cliente.getIdCliente());
                session.setAttribute("nomUsuario", nomUsuario);
                
                //NO SE QUE PONER SI POR DEFECTO QUE RECUERDE AL USUARIO O MEJOR QUE NO LO HAGA, DE MOMENTO OPTO POR LO SEGUNDO
                //crear cookies "otUser" y "otPass"
                //gestionCookies("crear");
                
                //limpiar contenido de estos atributos
                email ="";
                password = "";
                nomReg = "";
                apeReg = "";
                fecNacReg = null;
                
            }//if
                                    
        } else {
            //si (si se produjo un error al escribir en la BD) --> informar usuario en el dialogo (usuario ya existente probablemente)
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email y/o contraseña incorrectos", "");
        }//if (resultado) else
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loginCorrecto", loginCorrecto);                                                                     
        
    }//metodo    
    
            
    /**
     * Metodo al que se llama para cerrar la sesion actual del usuario. También se borran las cookies.
     * MEJORAR: Pedir confirmación.
     */
    public void logout() {
        
        //invalidar la sesion del usuario actual
        //sacado de http://www.javaknowledge.info/authentication-based-secure-login-logout-using-jsf-2-0-and-primefaces-3-4-1/
        HttpSession session = UtilSesion.getSession();
        session.invalidate();
        
        //borrar cookies (si existen)
        borrarUUID();
        
        //tras pulsar el logout redireccionar a la pagina de inicio
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(IndexViewMB.class.getName()).log(Level.SEVERE, null, ex);
        }//try..catch
        FacesContext.getCurrentInstance().responseComplete();        
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Gracias por visitar OmegaTek", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }//metodo    
    
        
    /**
     * Se comprueba la existencia de cookies en el pc del cliente, de visitas anteriores.
     * Pueden existir las cookies 'otR' (indica que el usuario activo la opcion 'recordarme' en el dialogo de login) y 
     * 'otId' (que es un UUID generado en una visita previa del usuario a la web, que srive para identenficiarlo, y que esta almacenado tb en la BD).
     *
     * Primero comprobamos si existe el atributo userID en la sesion, lo que nos indicaria que ya se ha iniciado la sesion; y no es necesario realizar
     * el proceso de login de nuevo.
     * Si no existe el atributo, obtenemos el array de cookies y buscamos las cookies 'otR' y 'otId'.
     * Si no las encontramos, no hacemos nada.
     * Si las encontramos, obtenemos de la BD el usuario correspondiente con el token 'otId'.
     * Y ahora podiamos hacer dos cosas:
     * - como hasta ahora, obtener email y password del usuario de ese objeto y pasar contro a login, PERO SERIA REDUNDANTE
     * - crear un nuevo metodo posLogin, con las operaciones que se hacen cuando el login ha sido exitoso; y pasamos el ontrol a este nuevo metodo
     *
     *Tambien crearemos una cookie: 'otCheck' para que no se pase por este método todas las veces que se carge la vista 'index'; si no solo la 1a vez
     */    
    public void comprobarCookies() {
        
        DAOGenerico oDAO = new DAOGenerico();
        String token = null;
        boolean otR = false;
        String cookieName;
        Cookie cookie[] = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getCookies();
        
        //sesion NO iniciada??
        if (UtilSesion.getUserID() == null) {
            //si --> hay cokies?
            if (cookie != null && cookie.length > 0) {
                //si --> buscar las que nos interesan
                for (Cookie c : cookie) {
                    //si --> hay alguna que tengan de nombre otR y/o otId
                    cookieName = c.getName();
                    if ("otR".equals(cookieName)) {
                        //si --> el usuario activo la opción recordarme en una visita anterior a la web
                        otR = true;
                    }                
                    if ("otId".equals(cookieName)) {
                        //si --> guardar el valor de token
                        token = c.getValue();
                    }
                }//for
            }//if (hay cookies)            
            
            //estaban las cookies que nos interesaban?
            if (otR && token != null) {
                //si --> obtener de la BD el usuario correspondiente con el token contenido en token (otId)
                String clausulaWhere = String.format("where Token = '%s'", token);
                Usuario u = (Usuario) oDAO.leer("", "Usuario", clausulaWhere, -1, -1).iterator().next();
                
                //NO SEGURO QUE HACER SI U == NULL, probablemente mostar un mensaje de error o simplemente no hacer nada
                if (u != null) {
                    posLogin(u);
                }//if (u!=null)
                
            }//if (otR && token != null) else
            
        } else {
            //no --> sesion ya inciada
            //no hacer nada
        }//if (sesion NO iniciada??) else
        
        Cookie otCheck = new Cookie("otCheck","1");
        otCheck.setMaxAge(-1);      //poniendole -1 se borrara al cerrar la sesion actual (-1 = by end of current session, 0 = immediately expire it, otherwise just the lifetime in seconds.)
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otCheck);        
        
        System.out.printf("[INFO - MB] [AccesoMB] .comprobarCookies\n");
                                
    }//metodo

        
    /**
     * Generar un UUID; guardar ese UUID, en la tabla Usuarios de la BD y guardarlo tb en una cookie en el equipo del usuario,
     * junto con otra cookie, que indique que el usuario ha activado la opcion 'recordarme' en la pantalla de login.
     * @param u
     */
    public void generarUUID(Usuario u) {
        
        DAOGenerico oDAO = new DAOGenerico();
        
        //generar el uid
        UUID uid = UUID.randomUUID();                
        
        //guardar el token en la tabla usuario
        u.setToken(uid.toString().replaceAll("-", ""));
        oDAO.crearOactualizar(u);
        
        //generar las dos cookies necesarias
        Cookie otId = new Cookie("otId", uid.toString().replaceAll("-", ""));
        otId.setMaxAge(604800);   //3600->1hora // 86400->1dia // 604800->1 semana
        Cookie otR = new Cookie("otR", "1");
        otR.setMaxAge(604800);    //3600->1hora // 86400->1dia // 604800->1 semana    
        
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otId); 
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otR);        
                
        System.out.printf("[INFO - MB] [UsuarioMB] .generarUUID ejecutado.\n");        
    }//metodo
    
    
    /**
     * Borrar los cookies otId y otR (si existian). Y no seguro de si actualizar tb el objeto ususario para poner token a null.
     * Tb. borro 'otCheck'.
     */
    public void borrarUUID() {
        
        //borrar las dos cookies (si existian)
        Cookie otId = new Cookie("otId", null);
        otId.setMaxAge(0);
        Cookie otR = new Cookie("otR", null);
        otR.setMaxAge(0);
        Cookie otCheck = new Cookie("otCheck",null);
        otCheck.setMaxAge(0);
        
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otId); 
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otR);                
        ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(otCheck);                

        System.out.printf("[INFO - MB] [UsuarioMB] .borrarUUID ejecutado.\n");        
        
    }//metodo
    
    
    /**
     * Comprobar si el email introducido por el usuario, en el proceso de registro, ya esta siendo usado por otro usuario, o esta libre,
     * para registrarse con el. Aunque seria mas util, si el identificador de login fuese un nombre de usuario y no un email
     */
    public void buscarEmail() {
        
        DAOGenerico oDAO = new DAOGenerico();
        String clausulaWhere = String.format("where email = '%s'",email);
        Object objeto = oDAO.leer("", "Usuario", clausulaWhere, -1, -1);
        
        if (objeto != null) {
            //mensaje de que ese email ya esta siendo usado por otra persona
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email no disponible", "");
            FacesContext.getCurrentInstance().addMessage(null, message);            
            email = "";
        }//if
    }//buscarEmail
    
    
    //<editor-fold defaultstate="collapsed" desc="setters/getters/constructores">
    public AccesoViewMB() {
        System.out.println("[INFO - MB] [AccesoMB] Constructor ejecutado");        
    }//fin constructor
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordMatch() {
        return passwordMatch;
    }

    public void setPasswordMatch(String passwordMatch) {
        this.passwordMatch = passwordMatch;
    }
    
    public boolean isRecordarme() {
        return recordarme;
    }

    public void setRecordarme(boolean recordarme) {
        this.recordarme = recordarme;
    }

    public String getNomReg() {
        return nomReg;
    }

    public void setNomReg(String nomReg) {
        this.nomReg = nomReg;
    }

    public String getNifReg() {
        return nifReg;
    }

    public void setNifReg(String nifReg) {
        this.nifReg = nifReg;
    }

    public String getApeReg() {
        return apeReg;
    }

    public void setApeReg(String apeReg) {
        this.apeReg = apeReg;
    }

    public Date getFecNacReg() {
        return fecNacReg;
    }

    public void setFecNacReg(Date fecNacReg) {
        this.fecNacReg = fecNacReg;
    }
    //</editor-fold>    
    
}//CLASS
