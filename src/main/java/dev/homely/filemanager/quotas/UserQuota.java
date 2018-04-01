package dev.homely.filemanager.quotas;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserQuota {

    @Id
    private String id;

    @NonNull private Long memoryLimit;
    @NonNull private Long memoryAmount;
    @NonNull private String userId;
}
