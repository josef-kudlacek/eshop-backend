package cz.jkdabing.backend.entity.key;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ProductFormatKey implements Serializable {

    private UUID productId;

    private Long formatId;
}
