package eu.crg.ega.configservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import eu.crg.ega.microservice.filter.RestTokenPreAuthenticatedProcessingFilter;
import eu.crg.ega.microservice.security.RestTokenAuthenticationUserDetailsService;
import eu.crg.ega.microservice.security.RestWebAuthenticationDetailsSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  Environment environment;

  // REST token authentication Provider and filter
  @Bean
  public AuthenticationDetailsSource restWebAuthenticationDetailsSource() {
    return new RestWebAuthenticationDetailsSource();
  }

  @Bean
  public RestTokenPreAuthenticatedProcessingFilter restTokenPreAuthenticatedProcessingFilter(
      final AuthenticationManager authenticationManager) {
    RestTokenPreAuthenticatedProcessingFilter filter = new RestTokenPreAuthenticatedProcessingFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setInvalidateSessionOnPrincipalChange(true);
    filter.setCheckForPrincipalChanges(false);
    filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
    filter.setAuthenticationDetailsSource(restWebAuthenticationDetailsSource());
    return filter;
  }

  @Bean
  public RestTokenAuthenticationUserDetailsService restTokenAuthenticationUserDetailsService() {
    return new RestTokenAuthenticationUserDetailsService();
  }

  @Bean
  public AuthenticationProvider restTokenAuthenticationProvider() {
    PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
    provider.setPreAuthenticatedUserDetailsService(restTokenAuthenticationUserDetailsService());
    return provider;
  }
  //END REST token authentication Provider

  //Access Authentication Manager Bean
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  // END Access Authentication Manager Bean

  //CONFIGURATION
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // Add auth provider for token
    auth.authenticationProvider(restTokenAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    String apiVersion = environment.getProperty("server.servlet-path");

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Do not create sessions

    http.formLogin().disable();

    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers(apiVersion + "/info").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/metrics/**").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/dump").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/trace").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/mappings").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/config/**").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/autoconfig").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/beans").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/health").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/configprops").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/logs/**").hasAnyRole("ADMIN", "SYSTEM", "SYSTEM_BASIC")
        .antMatchers(apiVersion + "/login").permitAll();

    http.addFilterBefore(restTokenPreAuthenticatedProcessingFilter(authenticationManagerBean()),
        UsernamePasswordAuthenticationFilter.class);
  }
  //END CONFIGURATION
}
