package models.Profile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateProfileRequest {
    String fullName;
    String phone;
    String address;
    String bio;
//    String dateOfBirth;
//    String gender;
}
