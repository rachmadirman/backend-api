package com.izeno.backendapi.repository;

import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.entity.BatchTable;
import com.izeno.backendapi.entity.UserConfig;
import com.izeno.backendapi.model.PayloadRs;
import com.izeno.backendapi.utils.CommonUtils;
import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            List<BatchTable> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BatchTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DATA, TOTAL : {}]", result.size());
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
            List<BatchDetailTable> result = jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(BatchDetailTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA, TOTAL : {}]", result.size());
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
            List<BatchDetailTable> result = jdbcTemplate.query(sql, new Object[]{batchId},
                    new BeanPropertyRowMapper<>(BatchDetailTable.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }


    public List<UserConfig> fetchUserConfig() throws Exception {
        String sql = "SELECT * FROM POC_SAPURA.API_INGESTION.USER_TABLE";

        try {
            List<UserConfig> result = jdbcTemplate.query(sql,  new BeanPropertyRowMapper<>(UserConfig.class));
            log.info("[SUCCESS FETCH BATCH TABLE DETAIL DATA, TOTAL : {}]", result.size());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public PayloadRs updateUserConfig(UserConfig req) throws Exception {

        PayloadRs payloadRs = new PayloadRs();
        String sql = "UPDATE POC_SAPURA.API_INGESTION.USER_TABLE SET \"apiurl\" = ?, \"clientid\" = ?, \"token\" = ?, \"environment\" = ?";

        try {
            int  result = jdbcTemplate.update(sql,  req.getApiurl(), req.getClientid(), req.getToken(), req.getEnvironment());

            payloadRs.setStatus("Success");
            payloadRs.setStatusDescription("Success update LHDN Config");
            payloadRs.setStatusCode(200);

            return payloadRs;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO GET BATCH TABLE DATA : {}]", e.getMessage());
            throw new Exception();
        }
    }

    public int insertBatchTable(String filename, String batchid) throws Exception {
        String sql = "INSERT INTO POC_SAPURA.API_INGESTION.BATCH_TABLE\n" +
                "(\"batchid\", \"csvfilename\", \"start_date\", \"end_date\", \"status\", \"submissionuuid\") VALUES\n"
                +
                "(?, ?, ?, ?, ?, ?)";

        try {
            int result = jdbcTemplate.update(sql, batchid, filename, CommonUtils.getCurrentDate(), null, "INPROGRESS",
                    null);
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
            String uuid) throws Exception {
        String sql = "INSERT INTO POC_SAPURA.API_INGESTION.BATCH_DETATIL_TABLE (\"einvoicenumber\", \"einvoicedocument\", \"requestdate\", \"validationstatus\", \"batchid\", \"uuid\")\n"
                +
                "VALUES (?,?, ?, ?, ?, ?)";

        try {
            int result = jdbcTemplate.update(sql, einvoicenumber, einvoicedocument, requestdate, validationstatus,
                    batchid, uuid);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[FAILED TO INSERT BATCH DETAILS TABLE DATA, BATCHID : {}, ERROR : {} ]", batchid,
                    e.getMessage());
            throw new Exception();
        }
    }
}
