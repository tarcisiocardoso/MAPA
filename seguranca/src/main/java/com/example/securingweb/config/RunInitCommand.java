package com.example.securingweb.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.securingweb.model.Usuario;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RunInitCommand implements CommandLineRunner {
	  private static final Logger log = LoggerFactory.getLogger(RunInitCommand.class);
	  
	  @Autowired
	  JdbcTemplate jdbcTemplate;
	  
	  @Override
	  public void run(String... strings) throws Exception {

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
