package com.embabel.prepper.file_rw.agent;

public abstract class FileRwDomain {

    public record FileWriteRequest(String filePath, String content) {}

    public record FileWriteResponse(String msg) {}
}
