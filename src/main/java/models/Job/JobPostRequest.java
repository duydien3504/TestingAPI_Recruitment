package models.Job;

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
public class JobPostRequest {
    String title;
    String description;
    String requirements;
    int category_id;
    int location_id;
    int level_id;
    int salary_min;
    int salary_max;
}
