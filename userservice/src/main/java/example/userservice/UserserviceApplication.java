package example.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

}

@RestController
class ApplicationController {
	Logger log = LoggerFactory.getLogger(ApplicationController.class);
	@GetMapping("/user/{userId}/accounts")
	public Mono<Account> accountsForUser(@PathVariable String userId) {
	  log.info("Retrieving accounts for user {}", userId);
	  return Mono.just(new Account("test", "a name"));
	}

	@GetMapping("/health")
	public Mono<String> health() {
		return Mono.just("UP");
	}
}

record Account(String id, String name) {}

@Configuration
@EnableWebFluxSecurity
class Config {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange(auth -> auth.pathMatchers("/health").permitAll())
				.authorizeExchange(auth -> auth.anyExchange().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		http.csrf(ServerHttpSecurity.CsrfSpec::disable);
		return http.build();
	}

//	@Bean
//	public ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.client.provider.spring.issuer-uri}") String issuerUri) {
//		return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
//	}
}