package br.gov.mapa.seguranca.controller;

import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController  {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,  Model model) {
        System.out.println("============================ERROR===========================");
        
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        Enumeration<String> enume = request.getAttributeNames();
        while( enume.hasMoreElements() ) {
        	String nome = enume.nextElement();
        	
        	System.out.println(nome+": "+ request.getAttribute(nome).toString());
        }
        
        
        
        
        
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