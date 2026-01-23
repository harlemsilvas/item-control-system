package br.com.harlemsilvas.itemcontrol.core.application.usecases.alert;

import br.com.harlemsilvas.itemcontrol.core.application.ports.AlertRepository;
import br.com.harlemsilvas.itemcontrol.core.application.ports.ItemRepository;
import br.com.harlemsilvas.itemcontrol.core.domain.model.Alert;

import java.util.Objects;

/**
 * Use Case para criar um novo alerta.
 */
public class CreateAlertUseCase {

    private final AlertRepository alertRepository;
    private final ItemRepository itemRepository;

    public CreateAlertUseCase(AlertRepository alertRepository, ItemRepository itemRepository) {
        this.alertRepository = Objects.requireNonNull(alertRepository, "AlertRepository cannot be null");
        this.itemRepository = Objects.requireNonNull(itemRepository, "ItemRepository cannot be null");
    }

    /**
     * Executa o caso de uso de criação de alerta.
     *
     * @param alert Alerta a ser criado
     * @return Alerta criado
     * @throws IllegalArgumentException se o alerta for nulo
     * @throws ItemNotFoundException se o item associado não existir
     */
    public Alert execute(Alert alert) {
        if (alert == null) {
            throw new IllegalArgumentException("Alert cannot be null");
        }

        // Verificar se o item existe
        if (!itemRepository.existsById(alert.getItemId())) {
            throw new ItemNotFoundException("Item not found with id: " + alert.getItemId());
        }

        return alertRepository.save(alert);
    }

    /**
     * Exception lançada quando um item não é encontrado.
     */
    public static class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
}
