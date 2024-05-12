package org.javaacademy.onlinebank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server moneyBankServer = new Server();
        moneyBankServer.setUrl("http://localhost:8082");
        moneyBankServer.setDescription("LLC MoneyBank сервер");

        Server euroBankServer = new Server();
        euroBankServer.setUrl("http://localhost:8001");
        euroBankServer.setDescription("LLC EuroBank сервер");

        Contact contact = new Contact();
        contact.setEmail("firebolt182@mail.ru");
        contact.setName("Alexander Monaenkov");

        Info info = new Info()
                .title("Онлайн-банк сервис")
                .version("1.0")
                .contact(contact)
                .description("Апи по онлайн банкингу");

        return new OpenAPI().info(info).servers(List.of(moneyBankServer, euroBankServer));

    }
}
