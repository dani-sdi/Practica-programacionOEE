package com.app.practicaOEEDaniel;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.app.practicaOEEDaniel.seguridad.JWTAuthorizationFilter;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PracticaOeeDanielApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticaOeeDanielApplication.class, args);
	}
    
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//Las peticiones recibidas pasaran por el filtro para comprobar que llevan un token correcto.
			http.cors().and().csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/usuarios/login").permitAll() // /login y /guardar no se exvluyen de esta comprobación
				.antMatchers(HttpMethod.POST, "/usuarios/guardar").permitAll()
				.anyRequest().authenticated();
			
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		}
		/*
		 * Permite las peticiones recibidas desde el servidor localhost:4200
		 */
	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                .allowedOrigins("http://localhost:4200")
	                .allowedMethods("*")
	                .allowedHeaders("*");
	            }
	        };
	    }
	}

}
