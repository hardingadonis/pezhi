package utils;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Config {

    @SerializedName("database_location")
    private String databaseLocation;
}