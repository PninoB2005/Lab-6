package data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.DisponibilidadEquipos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DisponibilidadEquiposHttp implements DisponibilidadEquipos {

    private final String urlBase;
    private final HttpClient cliente;
    private final ObjectMapper mapper;

    public DisponibilidadEquiposHttp(String urlBase) {
        this.urlBase = urlBase.replaceAll("/+$", "");
        this.cliente = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean estaDisponible(int equipoId) {

        String url = urlBase + "/api/v1/equipos/" + equipoId;

        HttpRequest peticion = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> respuesta = cliente.send(
                    peticion,
                    HttpResponse.BodyHandlers.ofString());

            int codigo = respuesta.statusCode();

            if (codigo == 404) {
                return false;
            }

            if (codigo != 200) {
                throw new ServicioNoDisponible("HTTP " + codigo);
            }

            JsonNode json = mapper.readTree(respuesta.body());

            return "disponible".equals(
                    json.path("estado").asText());

        } catch (IOException e) {
            throw new ServicioNoDisponible(e.getMessage(), e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServicioNoDisponible(e.getMessage(), e);
        }
    }
}
