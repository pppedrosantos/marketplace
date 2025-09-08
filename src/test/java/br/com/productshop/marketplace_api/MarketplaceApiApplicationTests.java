package br.com.productshop.marketplace_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class MarketplaceApiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // Verifica se o contexto foi injetado corretamente
        assertNotNull(applicationContext, "O contexto da aplicação deve ser carregado");
    }

    @Test
    void applicationStarts() {
        // Verifica se a aplicação inicia sem erros
        MarketplaceApiApplication.main(new String[]{"--spring.profiles.active=test"});
    }

    @Test
    void applicationHasAllRequiredBeans() {
        // Verifica se os principais beans da aplicação estão presentes
        assertNotNull(applicationContext.getBean(MarketplaceApiApplication.class),
            "O bean MarketplaceApiApplication deve estar presente");

        // Verifica o número de beans carregados
        assertNotNull(applicationContext.getBeanDefinitionNames(),
            "Deve haver beans definidos no contexto");
    }
}
