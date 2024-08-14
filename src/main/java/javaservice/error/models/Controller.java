package javaservice.error.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "controllers")
public class Controller extends BaseEntity {
    private UUID guid;
    private String serialNumber;
    private String vehicleNumber;
}
