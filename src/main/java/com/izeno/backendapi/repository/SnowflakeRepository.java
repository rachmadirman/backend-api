package com.izeno.backendapi.repository;

import com.izeno.backendapi.entity.*;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.utils.CommonUtils;
import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class SnowflakeRepository {

    @Autowired
    @Qualifier("snowflakeJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<BatchTable> fetchBatchTable() throws Exception {
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.BATCH_TABLE ORDER BY \"start_date\" DESC ";

        try {
            List<BatchTable> resultQuery = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BatchTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DATA, TOTAL : {}]", resultQuery.size());

            List<BatchTable> result = new ArrayList<>();
            for (BatchTable b : resultQuery){
                BatchTable data = new BatchTable();
                data.setBatchid(b.getBatchid());
                data.setCsvfilename(b.getCsvfilename());
                data.setStart_date(CommonUtils.formatDate(b.getStart_date()));
                data.setEnd_date(CommonUtils.formatDate(b.getEnd_date()));
                data.setStatus(b.getStatus());
                data.setSubmissionuuid(b.getSubmissionuuid());
                data.setFileLink(b.getFileLink());

                result.add(data);
            }


            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public List<BatchDetailTable> fetchBatchDetailTable() throws Exception {
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE ORDER BY \"requestdate\" DESC ";

        try {
            List<BatchDetailTable> resultQuery = jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(BatchDetailTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA, TOTAL : {}]", resultQuery.size());

            List<BatchDetailTable> result = new ArrayList<>();
            for (BatchDetailTable b : resultQuery){
                BatchDetailTable data = new BatchDetailTable();
                data.setEinvoicenumber(b.getEinvoicenumber());
                data.setRequestdate(CommonUtils.formatDate(b.getRequestdate()));

                result.add(data);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public List<BatchDetailTable> fetchBatchDetailTableById(String batchId) throws Exception {
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE WHERE \"batchid\" = ? ORDER BY \"requestdate\" DESC";

        try {
            List<BatchDetailTable> resultQuery = jdbcTemplate.query(sql, new Object[]{batchId},
                    new BeanPropertyRowMapper<>(BatchDetailTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA, TOTAL : {}]", resultQuery.size());

            List<BatchDetailTable> result = new ArrayList<>();
            for (BatchDetailTable b : resultQuery){
                BatchDetailTable data = new BatchDetailTable();
                data.setEinvoicenumber(b.getEinvoicenumber());
                data.setRequestdate(CommonUtils.formatDate(b.getRequestdate()));

                result.add(data);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public List<BatchDetailTable> fetchBatchDetailTableByIdRejected(String batchId) throws Exception {
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE WHERE \"batchid\" = ? AND " +
                "\"validationstatus\" = 'REJECTED' ORDER BY \"requestdate\" DESC";

        try {
            List<BatchDetailTable> result = jdbcTemplate.query(sql, new Object[]{batchId},
                    new BeanPropertyRowMapper<>(BatchDetailTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA FOR REJECTED DOC, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public List<String> fetchInvoiceNoByIdRejected(String batchId) throws Exception {

        String sql = "SELECT \"einvoicenumber\"  FROM POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE " +
                "WHERE \"batchid\"  = ? AND \"validationstatus\"  = 'REJECTED'";

        try {

            List<String> result = jdbcTemplate.query(sql, new Object[]{batchId},
                    ((rs, rowNum) -> rs.getString("einvoicenumber")));

            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA FOR REJECTED DOC, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }


    public List<UserConfig> fetchUserConfig() throws Exception {
        String sql = "SELECT * FROM EINVOICE.API_INGESTION.USER_TABLE";

        try {
            List<UserConfig> result = jdbcTemplate.query(sql,  new BeanPropertyRowMapper<>(UserConfig.class));
            log.info("[SUCCESS FETCH LHDN CONFIG, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET LHDN CONFIG : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public PayloadRs updateUserConfig(UserConfig req) throws Exception {

        PayloadRs payloadRs = new PayloadRs();
        String sql = "UPDATE EINVOICE.API_INGESTION.USER_TABLE SET \"apiurl\" = ?, \"clientid\" = ?, \"token\" = ?, \"environment\" = ?";

        try {
            int  result = jdbcTemplate.update(sql,  req.getApiurl(), req.getClientid(), req.getToken(), req.getEnvironment());
            log.info("[SUCCESS UPDATE LHDN CONFIG, TOTAL : {}]", result);

            payloadRs.setStatus("Success");
            payloadRs.setStatusDescription("Success update LHDN Config");
            payloadRs.setStatusCode(200);

            return payloadRs;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO UPDATE LHDN CONFIG : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public int insertBatchTable(String filename, String batchid, String filelink) throws Exception {
        String sql = "INSERT INTO POC_SAPURA.API_INGESTION.BATCH_TABLE\n" +
                "(\"batchid\", \"csvfilename\", \"start_date\", \"end_date\", \"status\", \"submissionuuid\", \"file_link\") VALUES\n"
                +
                "(?, ?, ?, ?, ?, ?, ?)";

        try {
            int result = jdbcTemplate.update(sql, batchid, filename, CommonUtils.getCurrentDate(), null, "INPROGRESS",
                    null, filelink);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO INSERT BATCH TABLE DATA, BATCHID : {}, ERROR : {} ]", batchid, e.getMessage());
            throw new Exception();
        }
    }

    public int updateBatchTable(String status, String submissionuuid, String batchid) throws Exception {
        String sql = "UPDATE POC_SAPURA.API_INGESTION.BATCH_TABLE SET \"status\"=?, \"end_date\"=?, \"submissionuuid\"=? \n"
                +
                "WHERE \"batchid\" = ?";

        try {
            int result = jdbcTemplate.update(sql, status, CommonUtils.getCurrentDate(), submissionuuid, batchid);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO UPDATE BATCH TABLE DATA, BATCHID : {}, ERROR : {} ]", batchid, e.getMessage());
            throw new Exception();
        }
    }

    public int insertBatchDetailsTable(String einvoicenumber,
            String einvoicedocument,
            String requestdate,
            String validationstatus,
            String batchid,
            String uuid,
            String reason) throws Exception {
        String sql = "INSERT INTO POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE " +
                "(\"einvoicenumber\", \"einvoicedocument\", \"requestdate\", \"validationstatus\", \"batchid\", \"uuid\", \"reason\")\n" +
                "VALUES (?,?,?,?,?,?,?)";

        try {
            int result = jdbcTemplate.update(sql, einvoicenumber, einvoicedocument, requestdate, validationstatus,
                    batchid, uuid, reason);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO INSERT BATCH DETAILS TABLE DATA, BATCHID : {}, ERROR : {} ]", batchid,
                    e.getMessage());
            throw new Exception();
        }
    }

    public void insertCSVData(CSVData req) throws Exception {
        String sql = "INSERT INTO POC_SAPURA.API_INGESTION.CSV_DATA (\"suppliername\", \"suppliertin\", \"supplieremail\", \"buyername\", " +
                "\"buyertin\", \"buyeremail\", \n" +
                "\"einvoiceversion\", \"einvoicetypeCode\", \"einvoicenumber\", \"einvoicedate\", \"einvoicecurrencycode\")\n" +
                "VALUES\n" +
                "(?,?,?,?,?,?,?,?,?,?,?)";

        try {
            int result = jdbcTemplate.update(sql, req.getSuppliername(), req.getSuppliertin(), req.getSupplieremail(), req.getBuyername(),
                    req.getBuyertin(), req.getBuyeremail(), req.getEinvoiceversion(), req.getEinvoicetypeCode(), req.getEinvoicenumber(),
                    req.getEinvoicedate(), req.getEinvoicecurrencycode());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO INSERT BATCH DETAILS TABLE DATA, INVOICE NO : {}, ERROR : {} ]", req.getEinvoicenumber(),
                    e.getMessage());
            throw new Exception();
        }
    }

    public List<CSVData> fetchbyInvoiceNo(String sql) throws Exception {
        try {
            List<CSVData> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CSVData.class));
            log.info("[SUCCESS FETCH FROM TABLE CSV_DATA, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public void insertConfig(String account, String db, String schema, String user) throws Exception {
        try {

            String sql = "insert into einvoice.api_ingestion.provisioning_table(\"Account\", \"Warehouse\", " +
                    "\"Database\", \"Schema\", \"User\", \"Role\", \"CreationDate\", \"UpdateDate\")\n" +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";

            int result = jdbcTemplate.update(sql, account, "COMPUTE_WH", db, schema, user, "sysadmin", CommonUtils.getCurrentDate(), "");
            log.info("[INSERTED ROW USER CONFIG : {}]", result);

        }catch (Exception e){
            log.error("[FAILED INSERT CONFIG : {}]", e.getMessage());
            throw new Exception();
        }
    }
    public List<ProvUserEntity> getUserConfig(String user) throws Exception {
        String sql = "Select \"id\", \"Account\", \"Warehouse\", \"Database\", \"Schema\", \"User\", \"Role\", \"CreationDate\", \"UpdateDate\"\n" +
                "from EINVOICE.API_INGESTION.PROVISIONING_TABLE where \"User\" = ?";

        try {
            List<ProvUserEntity> provUserEntity = jdbcTemplate.query(sql, new Object[]{user},
                    new BeanPropertyRowMapper<>(ProvUserEntity.class));

            return provUserEntity;

        }catch (Exception e){
            e.printStackTrace();
            log.error("[FAILED INSERT CONFIG : {}]", e.getLocalizedMessage());
            throw new Exception();
        }
    }


}
