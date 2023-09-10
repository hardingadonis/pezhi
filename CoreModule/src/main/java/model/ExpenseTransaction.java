package model;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseTransaction extends Transaction {
    private Integer categoryID;
    private Integer targetID;
    private Integer debtID;
}
