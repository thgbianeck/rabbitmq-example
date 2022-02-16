package br.com.bnck.rabbitmqexample;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Criado utilizando IntelliJ IDEA.
 * Projeto: rabbitmq-example
 * Usuário: Thiago Bianeck (Bianeck)
 * Data: 15/02/2022
 * Hora: 23:24
 */
public class ProductorEventosDeportivos {
    public static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {


        ConnectionFactory connectionFactory = new ConnectionFactory();
        // Abrir conexão AMQ e estabelecer um canal
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {

            // Criar topic exchange evento-deportivos
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            //Pais: es, fr, usa
            List<String> paises = Arrays.asList("es", "fr", "usa");
            // esporte : futebol, Tenis, Voleibol
            List<String> esportes = Arrays.asList("futebol", "tenis", "voleibol");
            // Tipo de partida: Ao Vivo, Notícias
            List<String> eventTypes = Arrays.asList("aovivo", "noticias");
            int count = 1;
            // Enviar mensagens para o topic exchange evento-deportivos
            while (true) {
                shuffle(paises, esportes, eventTypes);
                String pais = paises.get(0);
                String esporte = esportes.get(0);
                String event = eventTypes.get(0);
                // routing-key -> pais.esporte.eventType
                String routingKey = String.format("%s.%s.%s", pais, esporte, event);

                final String message = "Evento " + count;
                System.out.println(String.format("Produzindo mensagem (%s): %s", routingKey, message) );
                channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                Thread.sleep(1000);
                count++;
            }

        }

    }

    private static void shuffle(List<String> paises, List<String> esportes, List<String> eventTypes) {
        Collections.shuffle(paises);
        Collections.shuffle(esportes);
        Collections.shuffle(eventTypes);
    }
}
