package com.izeno.backendapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"batch_id", "file_link","start_date time", "end_date time", "status", "csv_filename", "submission_uuid", "submitted_by", "tenant_id"})
public class BatchTable {

    @JsonProperty("batch_id")
    private String batchid;
    @JsonProperty("csv_filename")
    private String csvfilename;
    @JsonProperty("start_date time")
    private String start_date;
    @JsonProperty("end_date time")
    private String end_date;
    @JsonProperty("status")
    private String status;
    @JsonProperty("submission_uuid")
    private String submissionuuid;
    @JsonProperty("file_link")
    private String fileLink;
    @JsonProperty("submitted_by")
    private String submittedby;
    @JsonProperty("tenant_id")
    private String tenantid;
}
