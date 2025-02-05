package de.neuefische.backend.appuser;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "app-users")
public class AppUser {
    @MongoId
    private String id;
    private String username;
    private String password;
    private AppUserRole role;
}
