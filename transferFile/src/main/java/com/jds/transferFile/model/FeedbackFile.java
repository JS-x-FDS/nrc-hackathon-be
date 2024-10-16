package com.jds.transferFile.model;

import lombok.*;


@DataSourceDefinition()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class FeedbackFile {

    @Id
    private Long id;

    private String fileName;  // Tên của file

    @Lob
    private byte[] fileContent;
}
