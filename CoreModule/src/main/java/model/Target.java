package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Target {
    private Integer ID;
    private String name;
    private String description;
    private Integer targetAmount;
    private Integer currentBalance;
}
