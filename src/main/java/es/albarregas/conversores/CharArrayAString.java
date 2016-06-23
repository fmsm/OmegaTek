
package es.albarregas.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * Custom converter para que el codigo postal que viene en char[] de la base de datos se muestre como un string
 * @author Francisco
 */
@FacesConverter("charArrayAString")
public class CharArrayAString implements javax.faces.convert.Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        //comprobación de que no sea nulo o este vacio
        if (value == null || ((char[]) value).length == 0) {
            return null;
        }
        
        //pasarlo de char[] a string
        char[] temp = (char[]) value;
        return new String(temp);
         
    }//método
        
}//CLASS
