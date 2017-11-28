package de.martinspielmann.gitcms.gitcms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.ClientResourceLocator;
import com.atlassian.crowd.service.client.CrowdClient;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CrowdAuthenticationProvider crowdAuthProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().permitAll();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(crowdAuthProvider);
	}
	
	@Bean
	public ClientResourceLocator resourceLocator() {
		return new ClientResourceLocator("crowd.properties");
	}
	
	@Bean
	public ClientPropertiesImpl clientProperties() {
		return ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator());
	}
	
	@Bean
	public RestCrowdClientFactory crowdClientFactory() {
		return new RestCrowdClientFactory();
	}

	@Bean
	public CrowdClient crowdClient() {
		return crowdClientFactory().newInstance(clientProperties());
	}
}