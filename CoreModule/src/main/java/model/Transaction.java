package model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    private Integer ID;
    private LocalDateTime dateTime;
    private String description;
    private Integer amount;
    private String type;
    private Integer sourceWallet;
}
