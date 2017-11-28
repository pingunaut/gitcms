package de.martinspielmann.gitcms.gitcms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class GitResourceHandler extends WebMvcConfigurerAdapter{
   
	@Autowired
	private GitSource gitSource;
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
	}
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {		
        registry.addResourceHandler("/**/*").addResourceLocations("file:"+gitSource.getAbsolutePath()+"/");
    }
}