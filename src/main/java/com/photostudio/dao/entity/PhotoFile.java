package com.photostudio.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class PhotoFile {
    private String fileName;
    private InputStream fileDataStream;
}
