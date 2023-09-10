package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseCategory {
    private Integer ID;
    private String name;
    private String description;
    private Integer limitAmount;
}
