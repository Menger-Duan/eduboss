package com.eduboss.security;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.springframework.util.Assert;

import com.eduboss.domainVo.ResourceGrantedVo;

/**
 * @author lmj
 *
 */
public class AccessValueVoter implements AccessDecisionVoter {
	
	private final static Logger log = Logger.getLogger(AccessValueVoter.class);

	private AuthenticationTrustResolver authenticationTrustResolver;
	
	private UrlMatcher urlMatcher;
	
	public AccessValueVoter() {
		authenticationTrustResolver = new AuthenticationTrustResolverImpl();
		urlMatcher = new AntUrlPathMatcher();
	}

	private boolean isFullyAuthenticated(Authentication authentication) {
		return (!authenticationTrustResolver.isAnonymous(authentication) && !authenticationTrustResolver.isRememberMe(authentication));
	}

	public void setAuthenticationTrustResolver(AuthenticationTrustResolver authenticationTrustResolver) {
		Assert.notNull(authenticationTrustResolver, "AuthenticationTrustResolver cannot be set to null");
		this.authenticationTrustResolver = authenticationTrustResolver;
	}

	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)
				&& (AuthenticatedVoter.IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute()) || AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute()) || AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute()))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		int result = ACCESS_ABSTAIN;
		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;
				if (AuthenticatedVoter.IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())) {
					if (isFullyAuthenticated(authentication) && checkAccess(authentication, object)) {
						return ACCESS_GRANTED;
					}
				}

				if (AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isRememberMe(authentication) || isFullyAuthenticated(authentication)) {
						return ACCESS_GRANTED;
					}
				}

				if (AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isAnonymous(authentication) || isFullyAuthenticated(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
						return ACCESS_GRANTED;
					}
				}
			}
		}
		return result;
	}
	
	private boolean checkAccess(Authentication authentication, Object object) {
		if (authentication.getName().equalsIgnoreCase("admin"))
			return true;
		String url = ((FilterInvocation) object).getRequestUrl();
		if (urlMatcher.pathMatchesUrl("/login.action", url) 
				|| urlMatcher.pathMatchesUrl("/logout.action", url)
				|| urlMatcher.pathMatchesUrl("/index.html", url)
				|| url.contains("jquery")
				|| url.indexOf("/dwr") >= 0)
			return true;
		
		Collection<GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority authority : authorities) {
			ResourceGrantedVo res = (ResourceGrantedVo) authority;
			if (StringUtils.isNotBlank(res.getRurl()) && !"#".equals(res.getRurl()) && url.contains(res.getRurl())) {
				return true;
			}
		}
		log.error("Security Access Deny, URL="+url + ", USERNAME="+authentication.getName());
		return false;
	}
}