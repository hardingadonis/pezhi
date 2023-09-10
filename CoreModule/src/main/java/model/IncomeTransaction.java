package model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class IncomeTransaction extends Transaction {
    private Integer categoryID;
    private Integer targetID;
    private Integer debtID;
}
