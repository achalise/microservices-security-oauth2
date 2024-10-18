package example.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;


@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

}

@RestController
class ApplicationController {
	private final WebClient webClient;

	ApplicationController(WebClient webClient) {
		this.webClient = webClient;
	}
	Logger log = LoggerFactory.getLogger(ApplicationController.class);
	@GetMapping("/user/{userId}/accounts")
	public Mono<User> accountsForUser(@PathVariable String userId) {
		log.info("Retrieving accounts for user {}", userId);

		return webClient.get()
				.uri("/accounts").retrieve()
				.bodyToMono(Account.class)
				.map(account -> new User(userId, "testuser@email.com", "Joe", "Bloggs", List.of(account)));
	}

	@GetMapping("/user/{userId}")
	public Mono<User> getUser(@PathVariable String userId) {
	  log.info("Retrieving accounts for user {}", userId);
	  return Mono.just(new User(userId, "testuser@email.com", "Joe", "Bloggs", Collections.emptyList()));
	}

	@GetMapping("/health")
	public Mono<String> health() {
		return Mono.just("UP");
	}
}

record Account(String id, String name) {}
record User(String id, String email, String firstName, String lastName, List<Account> accounts) {}

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

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.filter(new ServerBearerExchangeFilterFunction())
				.baseUrl("http://localhost:8083")
				.build();
	}
}