package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"batch_id", "file_link","start_date", "end_date", "status", "csv_filename", "submission_uuid"})
public class BatchTable {

    @JsonProperty("batch_id")
    private String batchid;
    @JsonProperty("csv_filename")
    private String csvfilename;
    @JsonProperty("start_date")
    private String start_date;
    @JsonProperty("end_date")
    private String end_date;
    @JsonProperty("status")
    private String status;
    @JsonProperty("submission_uuid")
    private String submissionuuid;
    @JsonProperty("file_link")
    private String fileLink;
}
