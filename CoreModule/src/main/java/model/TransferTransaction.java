package model;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferTransaction extends Transaction {
    private Integer targetWallet;
}
