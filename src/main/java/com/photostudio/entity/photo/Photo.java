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
        return source.substring(0, source.indexOf("."));
    }
}
