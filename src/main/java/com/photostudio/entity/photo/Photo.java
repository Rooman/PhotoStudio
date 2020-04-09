package com.photostudio.entity.photo;

import lombok.Data;
import lombok.NonNull;

@Data
public class Photo {
    @NonNull
    private long id;
    @NonNull
    private String source;
    @NonNull
    private PhotoStatus photoStatus;

    public String getName() {
        String[] partsOfPath = source.split("/");
        String name = partsOfPath[partsOfPath.length - 1];

        return name.substring(0, name.indexOf("."));
    }
}
