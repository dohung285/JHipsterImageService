package com.dohung.img.web.rest.response;

public class UploadFileResponse {

    private Integer id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse() {}

    public UploadFileResponse(Integer id, String fileName, String fileDownloadUri, String fileType, long size) {
        this.id = id;
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    // Getters and Setters (Omitted for brevity)

    @Override
    public String toString() {
        return (
            "UploadFileResponse{" +
            "id=" +
            id +
            ", fileName='" +
            fileName +
            '\'' +
            ", fileDownloadUri='" +
            fileDownloadUri +
            '\'' +
            ", fileType='" +
            fileType +
            '\'' +
            ", size=" +
            size +
            '}'
        );
    }
}
