package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeCategory {
    private Integer ID;
    private String name;
    private String description;
}
