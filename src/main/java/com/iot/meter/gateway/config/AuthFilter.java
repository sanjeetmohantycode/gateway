package com.iot.meter.gateway.config;


import com.iot.meter.gateway.dto.UserDTO;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Unauthorized access prohibited");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
                throw new RuntimeException("Invalid authorization structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("Authentication Url with token")
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .map(userDTO -> {
                        exchange.getRequest().mutate().header("x-auth-user-id", userDTO.getUserId())
                                .header("x-auth-org-id", userDTO.getOrgId()).build();
                        return exchange;
                    }).flatMap(chain::filter);
        });
    }

    public static class Config {

    }
}
