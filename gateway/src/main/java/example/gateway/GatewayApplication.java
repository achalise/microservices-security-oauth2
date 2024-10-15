package example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.Random;

@SpringBootApplication
@RestController
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	@GetMapping(value = "/token")
	public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		return Mono.just(authorizedClient.getAccessToken().getTokenValue());
	}

	@GetMapping("/")
	public Mono<String> index(WebSession session) {
		return Mono.just(session.getId());
	}

	@GetMapping(value = "/login/authenticate")
	public Mono<String> authorize(WebSession session) {
		return Mono.just("SUCCESS1");
	}

	@GetMapping(value = "/inernal/accounts")
	public Mono<String> generateRandom(WebSession session) {
		Random random = new Random();
		int nextRandom = random.nextInt();
		return Mono.just("account-" + nextRandom);
	}

	@GetMapping(value = "/oauth2/code/messaging-client-oidc")
	public Mono<String> authorize() {
		return Mono.just("AUTHORIZED");
	}

}
