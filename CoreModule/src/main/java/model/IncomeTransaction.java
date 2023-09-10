package model;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IncomeTransaction extends Transaction {
    private Integer categoryID;
    private Integer targetID;
    private Integer debtID;
}
