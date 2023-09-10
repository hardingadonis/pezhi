package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Debt {
    private Integer ID;
    private String fullName;
    private String description;
    private Integer debtAmount;
    private String phone;
}
