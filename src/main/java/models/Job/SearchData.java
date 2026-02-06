package models.Job;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.awt.*;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SearchData {
    String jobPostId;
    String companyId;
    String categoryId;
    String locationId;
    String levelId;
    String title;
    String description;
    String requirements;
    String salaryMin;
    String salaryMax;
    String status;
    String expiredAt;
    String editCount;
    String rejectionReason;
    boolean isDeleted;
    String created_at;
    Company company;
    Location location;
}
