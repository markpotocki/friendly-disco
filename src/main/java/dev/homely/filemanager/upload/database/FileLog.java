package dev.homely.filemanager.upload.database;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Date;

@Document
@Data
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class FileLog {

    @Id
    private String id;

    @NonNull private String fileName;

    @NonNull private String filePath;

    @Builder.Default private Date uploadDate = new Date();

    @NonNull @Indexed private String uploader;

    private Long size;
}
