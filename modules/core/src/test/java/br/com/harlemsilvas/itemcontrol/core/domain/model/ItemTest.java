package br.com.harlemsilvas.itemcontrol.core.domain.model;

import br.com.harlemsilvas.itemcontrol.core.domain.enums.ItemStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ItemTest {

    @Test
    void shouldCreateItemWithBuilder() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        Item item = new Item.Builder()
            .userId(userId)
            .name("Honda CB 500X")
            .nickname("Motoca")
            .templateCode("VEHICLE")
            .addTag("moto")
            .addTag("honda")
            .addMetadata("currentKm", 15000)
            .build();

        // then
        assertThat(item.getId()).isNotNull();
        assertThat(item.getUserId()).isEqualTo(userId);
        assertThat(item.getName()).isEqualTo("Honda CB 500X");
        assertThat(item.getNickname()).isEqualTo("Motoca");
        assertThat(item.getTemplateCode()).isEqualTo("VEHICLE");
        assertThat(item.getStatus()).isEqualTo(ItemStatus.ACTIVE);
        assertThat(item.getTags()).containsExactlyInAnyOrder("moto", "honda");
        assertThat(item.getMetadata().get("currentKm")).isEqualTo(15000);
        assertThat(item.getCreatedAt()).isNotNull();
        assertThat(item.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // when / then
        assertThatThrownBy(() -> new Item.Builder()
            .name("Test Item")
            .templateCode("VEHICLE")
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("UserId cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // when / then
        assertThatThrownBy(() -> new Item.Builder()
            .userId(UUID.randomUUID())
            .templateCode("VEHICLE")
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Name cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenTemplateCodeIsNull() {
        // when / then
        assertThatThrownBy(() -> new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("TemplateCode cannot be null or empty");
    }

    @Test
    void shouldUpdateMetadata() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .addMetadata("currentKm", 10000)
            .build();

        Map<String, Object> newMetadata = new HashMap<>();
        newMetadata.put("currentKm", 15000);
        newMetadata.put("brand", "Honda");

        // when
        item.updateMetadata(newMetadata);

        // then
        assertThat(item.getMetadata().get("currentKm")).isEqualTo(15000);
        assertThat(item.getMetadata().get("brand")).isEqualTo("Honda");
    }

    @Test
    void shouldUpdateSingleMetric() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .addMetadata("currentKm", 10000)
            .build();

        // when
        item.updateMetric("currentKm", 15000);

        // then
        assertThat(item.getMetadata().get("currentKm")).isEqualTo(15000);
    }

    @Test
    void shouldAddTag() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .build();

        // when
        item.addTag("honda");
        item.addTag("MOTO");

        // then
        assertThat(item.getTags()).containsExactlyInAnyOrder("honda", "moto");
    }

    @Test
    void shouldRemoveTag() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .addTag("honda")
            .addTag("moto")
            .build();

        // when
        item.removeTag("honda");

        // then
        assertThat(item.getTags()).containsExactly("moto");
    }

    @Test
    void shouldArchiveItem() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .build();

        // when
        item.archive();

        // then
        assertThat(item.getStatus()).isEqualTo(ItemStatus.ARCHIVED);
        assertThat(item.isActive()).isFalse();
    }

    @Test
    void shouldInactivateItem() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .build();

        // when
        item.inactivate();

        // then
        assertThat(item.getStatus()).isEqualTo(ItemStatus.INACTIVE);
        assertThat(item.isActive()).isFalse();
    }

    @Test
    void shouldActivateItem() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .status(ItemStatus.INACTIVE)
            .build();

        // when
        item.activate();

        // then
        assertThat(item.getStatus()).isEqualTo(ItemStatus.ACTIVE);
        assertThat(item.isActive()).isTrue();
    }

    @Test
    void shouldBeEqualWhenSameId() {
        // given
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Item item1 = new Item.Builder()
            .id(id)
            .userId(userId)
            .name("Item 1")
            .templateCode("VEHICLE")
            .build();

        Item item2 = new Item.Builder()
            .id(id)
            .userId(userId)
            .name("Item 2")
            .templateCode("UTILITY_BILL")
            .build();

        // then
        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        // given
        UUID userId = UUID.randomUUID();

        Item item1 = new Item.Builder()
            .userId(userId)
            .name("Item 1")
            .templateCode("VEHICLE")
            .build();

        Item item2 = new Item.Builder()
            .userId(userId)
            .name("Item 1")
            .templateCode("VEHICLE")
            .build();

        // then
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void shouldReturnUnmodifiableCollections() {
        // given
        Item item = new Item.Builder()
            .userId(UUID.randomUUID())
            .name("Test Item")
            .templateCode("VEHICLE")
            .addTag("test")
            .addMetadata("key", "value")
            .build();

        // when / then
        assertThatThrownBy(() -> item.getTags().add("new-tag"))
            .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> item.getMetadata().put("newKey", "newValue"))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
