package br.com.harlemsilvas.itemcontrol.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/database")
@RequiredArgsConstructor
@Tag(name = "Database Admin", description = "Endpoints administrativos para gerenciar o banco de dados")
public class DatabaseAdminController {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initCollections() {
        log.info("Verificando e criando collections...");

        String[] collections = {"items", "categories", "alerts", "events", "rules"};

        for (String collectionName : collections) {
            if (!mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.createCollection(collectionName);
                log.info("Collection '{}' criada", collectionName);
            } else {
                log.debug("Collection '{}' já existe", collectionName);
            }
        }

        log.info("Total de collections: {}", mongoTemplate.getCollectionNames().size());
    }

    @GetMapping("/collections")
    @Operation(summary = "Listar collections", description = "Lista todas as collections existentes no banco")
    public ResponseEntity<Map<String, Object>> listCollections() {
        Set<String> collections = mongoTemplate.getCollectionNames();

        Map<String, Object> response = new HashMap<>();
        response.put("database", mongoTemplate.getDb().getName());
        response.put("collections", collections);
        response.put("total", collections.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/collections/create")
    @Operation(summary = "Criar collections", description = "Força a criação das collections principais")
    public ResponseEntity<Map<String, String>> createCollections() {
        Map<String, String> result = new HashMap<>();

        String[] collections = {"items", "categories", "alerts", "events", "rules"};

        for (String collectionName : collections) {
            if (!mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.createCollection(collectionName);
                result.put(collectionName, "criada");
            } else {
                result.put(collectionName, "já existia");
            }
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Verificar conexão", description = "Verifica se a conexão com MongoDB está ativa")
    public ResponseEntity<Map<String, String>> checkConnection() {
        Map<String, String> status = new HashMap<>();

        try {
            mongoTemplate.getDb().getName();
            status.put("status", "conectado");
            status.put("database", mongoTemplate.getDb().getName());
        } catch (Exception e) {
            status.put("status", "erro");
            status.put("message", e.getMessage());
        }

        return ResponseEntity.ok(status);
    }
}