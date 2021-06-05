package com.example.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;

@RestController
public class AllController {
    @Autowired
    ResourceLoader resourceLoader;

    @GetMapping(path="/greeting")
    @CrossOrigin(origins = "*")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(1,"lalalal");
    }
    
    @GetMapping(path="/area-basica/usuarioLogado", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object usuarioLogado() {
        System.out.println(">>>>>usuarioLogado<<<<");
        
//        HashMap map = makeTac();
        HashMap map = makeHudson();
        
        sleep(300);
        
		return map;
	}
    
    @GetMapping(path="/area-segura/veiculo/crv/buscaValores", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object buscaValores() {
    	
        
    	HashMap map = new HashMap<String, Object>();
        map.put("cancelarAtpve", 45.00f);
        map.put("valorTotal", 161.00f);
        
        sleep(2000);
        
		return map;
	}
    
    private HashMap<String, Object> makeHudson(){
    	String []roles = {};
    	 HashMap map = new HashMap<String, Object>();
         map.put("nome", "HUDSON LUIS DE AVILA MINERVINI");
         map.put("cpf", "89437675104");
         map.put("email", "hudson.minervini@gmail.com");
         map.put("telefone", "(61) 981750355");
         map.put("imagem64", "\"PGh0bWw+DQo8aGVhZD48dGl0bGU+MzAxIE1vdmVkIFBlcm1hbmVudGx5PC90aXRsZT48L2hlYWQ+DQo8Ym9keT4NCjxjZW50ZXI+PGgxPjMwMSBNb3ZlZCBQZXJtYW5lbnRseTwvaDE+PC9jZW50ZXI+DQo8aHI+PGNlbnRlcj5uZ2lueDwvY2VudGVyPg0KPC9ib2R5Pg0KPC9odG1sPg0K\"");
         map.put("roles", roles);
         return map;
    }
    private HashMap<String, Object> makeTac(){
    	String []roles = {};
    	 HashMap map = new HashMap<String, Object>();
         map.put("nome", "Tarcisio Cardoso");
         map.put("cpf", "78424224191");
         map.put("email", "tarCardoso@gmail.com");
         map.put("telefone", "(061) 999732995");
         map.put("imagem64", "PGh0bWw+DQo8aGVhZD48dGl0bGU+MzAxIE1vdmVkIFBlcm1hbmVudGx5PC90aXRsZT48L2hlYWQ+DQo8Ym9keT4NCjxjZW50ZXI+PGgxPjMwMSBNb3ZlZCBQZXJtYW5lbnRseTwvaDE+PC9jZW50ZXI+DQo8aHI+PGNlbnRlcj5uZ2lueDwvY2VudGVyPg0KPC9ib2R5Pg0KPC9odG1sPg0K");
         map.put("roles", roles);
         return map;
    }

    @GetMapping(path="/area-segura/veiculo/crv/consultarStatusAtpv", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object consultarStatusAtpv(final HttpServletRequest request) {

        String cpf = request.getParameter("cpf");
        System.out.println("=====>"+ cpf );

        return resourceLoader.getResource(
          "classpath:data/veiculo.json");
    }
                                               
    @GetMapping(path="/area-segura/veiculo/crv/consultarStatusAtpveComprador", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object consultarStatusAtpveComprador(final HttpServletRequest request, HttpServletResponse response) {

        String cpf = request.getParameter("cpf");
        System.out.println("=====>"+ cpf );
        try {
	        Reader reader = new InputStreamReader(
	                resourceLoader.getResource("classpath:data/atpvComprador.json").getInputStream(), "UTF-8"
	            );
	        String s = FileCopyUtils.copyToString(reader);
	        
	        JSONArray arr = new JSONArray(s);
	        JSONArray arrRetorno = new JSONArray();
	        int qtd = 0;
	        for( int i=0; i< arr.length(); i++) {
	        	JSONObject j = arr.getJSONObject(i);
	        	System.out.println("--->"+ j.getString("docComprador"));
	        	if( j.getString("docComprador").equals(cpf)) {
	        		qtd++;
	        		arrRetorno.put( j );
	        	}
	        }
	        System.out.println(arrRetorno.toString() );
	        if( qtd > 0 ) return arrRetorno.toString(); // if( qtd > 10 ) return arrRetorno.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
	     
	    try {
			response.sendError(HttpStatus.NOT_FOUND.value(), "Sem ATPVE.");
		} catch (IOException e) {
			
		}
	    return null;   
    }
    
     
    @GetMapping(path="/area-segura/usuarioLogado/dadosBasicos", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object dadosBasicos(final HttpServletRequest request, HttpServletResponse response) throws IOException {

        String cpf = request.getParameter("cpf");
        System.out.println("=====>"+ cpf );
        
//        if( true) {
//        	response.sendError(HttpStatus.BAD_REQUEST.value(), "Prorrogacao não encontrado.");
//        	return null;
//        }

        sleep(1000);
        
        HashMap map = new HashMap<String, Object>();
        map.put("nome", "Tarcisio Cardoso");
        map.put("cpf", "78424224191");
        map.put("email", "tarCardoso@gmail.com");
        map.put("telefone", "(061) 999732995");

        map.put("questoes", new String[] {"aaa", "bbb", "ccc"});
        
        map.put("imagem64", "PGh0bWw+DQo8aGVhZD48dGl0bGU+MzAxIE1vdmVkIFBlcm1hbmVudGx5PC90aXRsZT48L2hlYWQ+DQo8Ym9keT4NCjxjZW50ZXI+PGgxPjMwMSBNb3ZlZCBQZXJtYW5lbnRseTwvaDE+PC9jZW50ZXI+DQo8aHI+PGNlbnRlcj5uZ2lueDwvY2VudGVyPg0KPC9ib2R5Pg0KPC9odG1sPg0K");
                
		return map;
    }
    
  
    @GetMapping(path="/area-segura/veiculo/crv/buscaTimeLineAtpve", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object buscaTimeLineAtpve(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        String renavam = request.getParameter("renavam");
        System.out.println("renavam =====>"+ renavam );
        
        if( renavam.equals("00846743211")) {
        	response.sendError(HttpStatus.NOT_FOUND.value(), "Prorrogacao não encontrado.");
        	return null;
        }
        
        sleep(1000);
        
        ArrayList<TimeLine>lst = new ArrayList<>();
        lst.add( new TimeLine("emissao", "Emissão da ATPV-e", true, "right"));
        lst.add( new TimeLine("assinaturaVendedor", "Assinatura eletrônica do vendedor", false, "right"));
        lst.add( new TimeLine("assinaturaComprador", "Assinatura eletrônica do comprador", false, "left"));
		return lst;
    }
    
    
    @GetMapping(path="/area-segura/veiculo/crv/cancelarAtpve", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object cancelarAssinatura(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        String renavam = request.getParameter("renavam");
        System.out.println("renavam =====>"+ renavam );
            
        sleep(2000);
 
        
        HashMap map = new HashMap<String, Object>();
    	map.put("numeroAtpv", "123321");
    	try {
    		byte[] inFileBytes = Files.readAllBytes(Paths.get("/home/tarcisio/trabalho/EDS/DETRAN/projetos/demo/src/main/resources/data/xx.pdf"));
		
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			
			String encodedString =  new String(encoded);
			
			 map.put("pdfAtpvBase64", encodedString);
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return map;
    }
    @GetMapping(path="/area-segura/veiculo/crv/emitirNovaAtpve", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object emitirNovaAtpve(final HttpServletRequest request, HttpServletResponse response) throws IOException {
        String renavam = request.getParameter("renavam");
        System.out.println("renavam =====>"+ renavam );
            
        sleep(2000);
        
        HashMap map = new HashMap<String, Object>();
    	map.put("numeroAtpv", "123321");
    	try {
    		byte[] inFileBytes = Files.readAllBytes(Paths.get("/home/tarcisio/trabalho/EDS/DETRAN/projetos/demo/src/main/resources/data/xx.pdf"));
		
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			
			String encodedString =  new String(encoded);
			
			 map.put("pdfAtpvBase64", encodedString);
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return map;
    }
    

    //area-segura/veiculo/retornaEnderecoPorCep?cep=73805125
    @GetMapping(path="/area-segura/veiculo/retornaEnderecoPorCep", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object retornaEnderecoPorCep(final HttpServletRequest request) {
    	String cep = request.getParameter("cep");
    	HashMap map = new HashMap<String, Object>();
        map.put("cep", cep);
        map.put("logradouro", "Rua 10 N 480");
        map.put("numero", "480");
        map.put("municipio", "Formosa");
        map.put("uf", "GO");
        map.put("bairro", "Primavera");
        map.put("codMunicipio", "123456");
        map.put("complemento", "casa 750");
        
		return map;
    }
    @PostMapping(path="/api/realizaMatch")
    @CrossOrigin(origins = "*")
    public Object realizaMatch(Object dado) {
    	HashMap map = new HashMap<String, Object>();
        map.put("confidence", "1");
        map.put("status", "202");
        
		return map;
    }
    @PostMapping(path="/area-publica/enviar-email")
    @CrossOrigin(origins = "*")
    public Object enviarEmail(Object dado) {
    	System.out.println("--->"+ dado );
    	
    	sleep(2000);
    	
    	HashMap map = new HashMap<String, Object>();
        map.put("aaa", "1");
        map.put("bbb", "202");
        
		return map;
    }
    
    
    @PostMapping(path="/area-segura/veiculo/crv/recusaAtpve")
    @CrossOrigin(origins = "*")
    public Object recusaAtpve(@RequestBody HashMap<String, String>dado) {
    	
    	
    	System.out.println("---->"+ dado );
    	
    	
    	HashMap map = new HashMap<String, Object>();
        sleep(1000);
		return map;
    }
    
    @PostMapping(path="/area-segura/veiculo/crv/registrarAssinatura")
    @CrossOrigin(origins = "*")
    public Object registrarAssinatura(Object dado) {
    	HashMap map = new HashMap<String, Object>();
        map.put("confidence", "1");
        map.put("status", "202");
        
        try {
        	Thread.sleep(2000);
        }catch(Exception e) {}
		return map;
    }
    
    private static void sleep(int tempo) {
    	try {
        	Thread.sleep(tempo);
        }catch(Exception e) {}
    }
    
    @PostMapping(path="/api/validaQuestoesUsuariov2")
    @CrossOrigin(origins = "*")
    public Object validaQuestoesUsuariov2(Object dado) {
    	HashMap map = new HashMap<String, Object>();
        map.put("confidence", "1");
        map.put("status", "202");
        
		return map;
    }
    
    @GetMapping(path="/area-segura/veiculo/crv/consultarPendenciaAtpv", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object consultarPendenciaAtpv(final HttpServletRequest request) {
    	String placa = request.getParameter("placa");
        try{

            Reader reader = new InputStreamReader(
                resourceLoader.getResource("classpath:data/veiculo.json").getInputStream(), "UTF-8"
            );

            String s = FileCopyUtils.copyToString(reader);


            sleep(1000);
//            JSONObject json = new JSONObject(s);
            
            JSONArray arr = new JSONArray(s);
            
            for( int i=0; i< arr.length(); i++) {
            	JSONObject j = arr.getJSONObject(i);
            	System.out.println("--->"+ j.getString("placa"));
            	if( j.getString("placa").equals(placa)) return j.toString();
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
		return null;
    }
    ///area-segura/veiculo/crv/atpv-eletronico/pdf?placa=JJU3172&renavam=00164603395"
    @GetMapping(path="/area-segura/veiculo/crv/atpv-eletronico/pdf", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object atpvEletronico(final HttpServletRequest request) {
    	
    	HashMap map = new HashMap<String, Object>();
    	map.put("numeroAtpv", "123321");
    	try {
    		byte[] inFileBytes = Files.readAllBytes(Paths.get("/home/tarcisio/trabalho/EDS/DETRAN/projetos/demo/src/main/resources/data/19F5F1B7-2058-4E94-B480-87A7FCDF8DE6.pdf"));
		
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			
			String encodedString =  new String(encoded);
			
			 map.put("pdfAtpvBase64", encodedString);
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return map;
    }    
    
    @GetMapping(path="/area-segura/veiculo/crv/emitirNovo-atpv-eletronico/pdf", produces = { "application/json"})
    @CrossOrigin(origins = "*")
	public Object emitirNovoAtpvEletronico(final HttpServletRequest request) {
    	
    	HashMap map = new HashMap<String, Object>();
    	map.put("numeroAtpv", "123321");
    	try {
    		byte[] inFileBytes = Files.readAllBytes(Paths.get("/home/tarcisio/trabalho/EDS/DETRAN/projetos/demo/src/main/resources/data/LICENCIAMENTO - 2021.pdf"));
		
			byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
			
			String encodedString =  new String(encoded);
			
			 map.put("pdfAtpvBase64", encodedString);
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return map;
    }
    
    public static class Greeting{
        public final long id;
        public final String content; 

        public Greeting(long i, String c){
            id=i;
            content = c;
        }
    }
    
    public static class TimeLine{
    	public String id;
    	public String titulo;
    	public boolean concluido;
    	public String position;
    	public TimeLine(String id, String tt, boolean ok, String pos) {
    		this.id = id;
    		this.titulo = tt;
    		this.concluido = ok;
    		this.position = pos;
    	}
    }
}
