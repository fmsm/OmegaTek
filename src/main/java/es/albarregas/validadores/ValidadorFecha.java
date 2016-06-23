
package es.albarregas.validadores;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Francisco
 */
public class ValidadorFecha implements Validator {
    
    /**
     * Comprobar que la fecha no sea mayor que la actual
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        //fecha de hoy
        LocalDate hoy = LocalDate.now();
        
        //obtener un objeto LocalDate a partir de la fecha que hay en el componente inputText
        Date fechaDate = (Date) value;
        LocalDate fechaLocalDate = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //LocalDate fechaDate = LocalDate.parse(fechaStr, formatoFecha);
        
        //la fecha es valida si es menor o igual al dia de hoy
        if (fechaLocalDate.isAfter(hoy)) {
            throw new ValidatorException( new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha debe ser anterior o igual al día de hoy", null) );
            //new FacesMessage(FacesMessage.SEVERITY_INFO, "Gracias por visitar OmegaTek", "");
        }
        
    }//validate
    
}//CLASS
