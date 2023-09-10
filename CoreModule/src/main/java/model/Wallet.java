package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wallet {
    private Integer ID;
    private String name;
    private String type;
    private Integer balance;
}