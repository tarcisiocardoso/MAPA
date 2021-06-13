package br.gov.mapa.segaut.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController  {

	@Autowired
	HttpServletRequest request;
	
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,  Model model) {
    	
    	Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	
    	String s = request.getRequestURI() +" ?"+ request.getQueryString() + " --> cd "+status;
        System.out.println("============================ERROR==========================="+s);
        
        
        
//        Enumeration<String> enume = request.getAttributeNames();
//        while( enume.hasMoreElements() ) {
//        	String nome = enume.nextElement();
//        	
//        	System.out.println(nome+": "+ request.getAttribute(nome).toString());
//        }
        
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("statusCode", statusCode);
            Exception e = (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if( e != null)
            model.addAttribute("erro", e.getMessage());
        }
        
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}