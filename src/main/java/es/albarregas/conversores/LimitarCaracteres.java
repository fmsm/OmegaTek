
package es.albarregas.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * Custom converter para que no se exceda un numero máximo de caracteres en los outputText del carousel de ultimas novedades
 * (este numero máximo esta definido por defecto en 30)
 * MEJORAR: QUE SE PUEDA PASAR COMO PARÁMETRO EL NUMERO MAXIMO DE CARACTERES
 * @author Francisco
 */
@FacesConverter("limitarCaracteres")
public class LimitarCaracteres implements javax.faces.convert.Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || ((String) value).isEmpty()) {
            return null;
        }

        String str = (String) value;
        
        if (str.length() > 25) {
            return str.substring(0, 25) + "...";
        } else {
            return str;
        }
        
    }//método
    
    
}//CLASS
