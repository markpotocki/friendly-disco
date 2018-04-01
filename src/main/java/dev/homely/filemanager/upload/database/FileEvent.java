package dev.homely.filemanager.upload.database;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;

@Document
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class FileEvent {

    @Id
    private String id;

    @NonNull private String fileId;
    @NonNull private String uploaderId;
    @NonNull private ActionType actionType;
    @Builder.Default private Date actionDate = new Date();
}
