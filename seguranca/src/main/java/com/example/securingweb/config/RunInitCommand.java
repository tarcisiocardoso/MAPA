package com.example.securingweb.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import com.example.securingweb.model.Usuario;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.directory.Attributes;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Configuration
public class RunInitCommand implements CommandLineRunner {
	  private static final Logger log = LoggerFactory.getLogger(RunInitCommand.class);
	  
	  @Autowired
	  JdbcTemplate jdbcTemplate;
	  
	  @Autowired
	  LdapTemplate ldapTemplate;
	  
	  @Override
	  public void run(String... strings) throws Exception {
		  
		  createTable();
		  runLdapQuery();
	  }
	  public void runLdapQuery() {
		  
		  boolean authenticated = ldapTemplate.authenticate("", "(uid=ben)", "123");
		  
		  System.out.println("authenticated: "+ authenticated);
		  
		  List<String> lst = ldapTemplate.search(
	         query().where("objectclass").is("person"),
	         new AttributesMapper<String>() {
	            public String mapFromAttributes(Attributes attrs)
	               throws NamingException, javax.naming.NamingException {
	               return attrs.get("cn").get().toString();
	            }
	         });
		  for(String s: lst) {
			  System.out.println("--->"+ s);
		  }
	  }
	  
	  public void createTable() {

	    log.info("Creating tables");

	    jdbcTemplate.execute("DROP TABLE usuario IF EXISTS");
	    jdbcTemplate.execute("CREATE TABLE usuario(" +
	        "id_usuario SERIAL, ds_login VARCHAR(255), ds_senha VARCHAR(12), nm_pessoa_fisica VARCHAR(255))");


	    // Uses JdbcTemplate's batchUpdate operation to bulk load data
	    jdbcTemplate.update("INSERT INTO usuario (ds_login, ds_senha, nm_pessoa_fisica) VALUES ('adm','123', 'Administrador')");
	    jdbcTemplate.update("INSERT INTO usuario (ds_login, ds_senha, nm_pessoa_fisica) VALUES ('marta','123', 'Marta')");

	    log.info("qyery:");
	    List<Usuario>lst = jdbcTemplate.query("select * from usuario", (rs, rowNum) ->
        new Usuario(
                rs.getLong("id_usuario"),
                rs.getString("ds_login"),
                rs.getString("ds_senha"),
                rs.getString("nm_pessoa_fisica")
        )	);
	    for( Usuario u: lst) {
	    	System.out.println(u);
	    }
	  }
}
