package model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Transaction {
    private Integer ID;
    private LocalDateTime dateTime;
    private String description;
    private Integer amount;
    private String type;
    private Integer sourceWallet;
}
