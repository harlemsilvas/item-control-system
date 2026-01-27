package br.com.harlemsilvas.itemcontrol.api.web.controller;

import org.springframework.boot.SpringBootConfiguration;

/**
 * Configuração mínima para testes WebMvc.
 *
 * Importante: NÃO registre controllers aqui.
 * O @WebMvcTest já registra o controller-alvo e os testes declaram @MockBean para as dependências.
 * Se incluirmos controllers via @ComponentScan, o Spring tentará instanciá-los sem os mocks e o contexto falha.
 */
@SpringBootConfiguration
public class TestWebMvcConfig {
}
