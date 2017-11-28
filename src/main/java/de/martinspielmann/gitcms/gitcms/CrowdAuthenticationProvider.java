package de.martinspielmann.gitcms.gitcms;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.ExpiredCredentialException;
import com.atlassian.crowd.exception.InactiveAccountException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.CrowdClient;

@Configuration
@ContextConfiguration(locations = { "classpath:/applicationContext-CrowdClient.xml" })

public class CrowdAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(CrowdAuthenticationProvider.class);

	@Autowired
	private CrowdClient crowdClient;
	
	@Override
	public Authentication authenticate(Authentication a) throws AuthenticationException {
		try {
			User authenticateUser = crowdClient.authenticateUser(a.getName(), a.getCredentials().toString());
			Collection<? extends GrantedAuthority> authorities = crowdClient
					.getNamesOfGroupsForUser(authenticateUser.getName(), 0, Integer.MAX_VALUE).stream()
					.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			return new UsernamePasswordAuthenticationToken(authenticateUser.getName(),
					null, authorities);
		} catch (UserNotFoundException | InactiveAccountException | ExpiredCredentialException
				| ApplicationPermissionException | InvalidAuthenticationException | OperationFailedException e) {
			LOG.info("Error authenticating user", e);
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (authentication.equals(UsernamePasswordAuthenticationToken.class));
	}
}