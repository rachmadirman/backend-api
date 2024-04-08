package com.izeno.backendapi.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.izeno.backendapi.entity.BatchDetailTable;
import com.izeno.backendapi.model.ForwardRequest.CsvContent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

public class CommonUtils {

    public static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");

        return currentDateTime.atZone(zoneId).format(formatter);
    }

    public static String stringToBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // just to test dynamic document
    public static String generateRequestFormat(CsvContent request) {
        String formRequest = "{\"_D\":\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\",\"_A\":\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\",\"_B\":\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\",\"Invoice\":[{\"UBLVersionID\":[{\"_\":\"2.1\"}],\"ID\":[{\"_\":\"TOSL108\"}],\"IssueDate\":[{\"_\":\""
                + request.getEinvoicedate() + "\"}],\"InvoiceTypeCode\":[{\"_\":"
                + request.getEinvoicetypecode()
                + ",\"listID\":\"UN/ECE 1001 Subset\",\"listAgencyID\":\"6\"}],\"Note\":[{\"_\":\"Ordered in our booth at the convention.\",\"languageID\":\"en\"}],\"TaxPointDate\":[{\"_\":\"2009-11-30\"}],\"DocumentCurrencyCode\":[{\"_\":\""
                + request.getEinvoicecurrencycode()
                + "\",\"listID\":\"ISO 4217 Alpha\",\"listAgencyID\":\"6\"}],\"AccountingCost\":[{\"_\":\"Project cost code 123\"}],\"InvoicePeriod\":[{\"StartDate\":[{\"_\":\"2009-11-01\"}],\"EndDate\":[{\"_\":\"2009-11-30\"}]}],\"OrderReference\":[{\"ID\":[{\"_\":\""
                + request.getEinvoicenumber()
                + "\"}]}],\"ContractDocumentReference\":[{\"ID\":[{\"_\":\"Contract321\"}],\"DocumentType\":[{\"_\":\"Framework agreement\"}]}],\"AdditionalDocumentReference\":[{\"ID\":[{\"_\":\"Doc1\"}],\"DocumentType\":[{\"_\":\"Timesheet\"}],\"Attachment\":[{\"ExternalReference\":[{\"URI\":[{\"_\":\"http://www.suppliersite.eu/sheet001.html\"}]}]}]},{\"ID\":[{\"_\":\"Doc2\"}],\"DocumentType\":[{\"_\":\"Drawing\"}],\"Attachment\":[{\"EmbeddedDocumentBinaryObject\":[{\"_\":\"UjBsR09EbGhjZ0dTQUxNQUFBUUNBRU1tQ1p0dU1GUXhEUzhi\",\"mimeCode\":\"application/pdf\"}]}]}],\"AccountingSupplierParty\":[{\"Party\":[{\"EndpointID\":[{\"_\":\"1234567890123\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}],\"PartyIdentification\":[{\"ID\":[{\"_\":\""
                + request.getSuppliertin()
                + "\",\"schemeID\":\"ZZZ\"}]}],\"PartyName\":[{\"Name\":[{\"_\":\"" + request.getSuppliername()
                + "\"}]}],\"PostalAddress\":[{\"ID\":[{\"_\":\"1231412341324\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}],\"Postbox\":[{\"_\":\"5467\"}],\"StreetName\":[{\"_\":\"Main street\"}],\"AdditionalStreetName\":[{\"_\":\"Suite 123\"}],\"BuildingNumber\":[{\"_\":\"1\"}],\"Department\":[{\"_\":\"Revenue department\"}],\"CityName\":[{\"_\":\"Big city\"}],\"PostalZone\":[{\"_\":\"54321\"}],\"CountrySubentityCode\":[{\"_\":\"RegionA\"}],\"Country\":[{\"IdentificationCode\":[{\"_\":\"DK\",\"listID\":\"ISO3166-1\",\"listAgencyID\":\"6\"}]}]}],\"PartyTaxScheme\":[{\"CompanyID\":[{\"_\":\"DK12345\",\"schemeID\":\"DKVAT\",\"schemeAgencyID\":\"ZZZ\"}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}],\"PartyLegalEntity\":[{\"RegistrationName\":[{\"_\":\"The Sellercompany Incorporated\"}],\"CompanyID\":[{\"_\":\"5402697509\",\"schemeID\":\"CVR\",\"schemeAgencyID\":\"ZZZ\"}],\"RegistrationAddress\":[{\"CityName\":[{\"_\":\"Big city\"}],\"CountrySubentity\":[{\"_\":\"RegionA\"}],\"Country\":[{\"IdentificationCode\":[{\"_\":\"DK\"}]}]}]}],\"Contact\":[{\"Telephone\":[{\"_\":\"4621230\"}],\"Telefax\":[{\"_\":\"4621231\"}],\"ElectronicMail\":[{\"_\":\""
                + request.getSupplieremail()
                + "\"}]}],\"Person\":[{\"FirstName\":[{\"_\":\"Antonio\"}],\"FamilyName\":[{\"_\":\"M\"}],\"MiddleName\":[{\"_\":\"Salemacher\"}],\"JobTitle\":[{\"_\":\"Sales manager\"}]}]}]}],\"AccountingCustomerParty\":[{\"Party\":[{\"EndpointID\":[{\"_\":\"1234567987654\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}],\"PartyIdentification\":[{\"ID\":[{\"_\":\""
                + request.getBuyertin()
                + "\",\"schemeID\":\"ZZZ\"}]}],\"PartyName\":[{\"Name\":[{\"_\":\"" + request.getBuyername()
                + "\"}]}],\"PostalAddress\":[{\"ID\":[{\"_\":\"1238764941386\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}],\"Postbox\":[{\"_\":\"123\"}],\"StreetName\":[{\"_\":\"Anystreet\"}],\"AdditionalStreetName\":[{\"_\":\"Back door\"}],\"BuildingNumber\":[{\"_\":\"8\"}],\"Department\":[{\"_\":\"Accounting department\"}],\"CityName\":[{\"_\":\"Anytown\"}],\"PostalZone\":[{\"_\":\"101\"}],\"CountrySubentity\":[{\"_\":\"RegionB\"}],\"Country\":[{\"IdentificationCode\":[{\"_\":\"BE\",\"listID\":\"ISO3166-1\",\"listAgencyID\":\"6\"}]}]}],\"PartyTaxScheme\":[{\"CompanyID\":[{\"_\":\"BE54321\",\"schemeID\":\"BEVAT\",\"schemeAgencyID\":\"ZZZ\"}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}],\"PartyLegalEntity\":[{\"RegistrationName\":[{\"_\":\"The buyercompany inc.\"}],\"CompanyID\":[{\"_\":\"5645342123\",\"schemeAgencyID\":\"ZZZ\",\"schemeID\":\"ZZZ\"}],\"RegistrationAddress\":[{\"CityName\":[{\"_\":\"Mainplace\"}],\"CountrySubentity\":[{\"_\":\"RegionB\"}],\"Country\":[{\"IdentificationCode\":[{\"_\":\"BE\"}]}]}]}],\"Contact\":[{\"Telephone\":[{\"_\":\"5121230\"}],\"Telefax\":[{\"_\":\"5121231\"}],\"ElectronicMail\":[{\"_\":\""
                + request.getBuyeremail()
                + "\"}]}],\"Person\":[{\"FirstName\":[{\"_\":\"John\"}],\"FamilyName\":[{\"_\":\"X\"}],\"MiddleName\":[{\"_\":\"Doe\"}],\"JobTitle\":[{\"_\":\"Purchasing manager\"}]}]}]}],\"PayeeParty\":[{\"PartyIdentification\":[{\"ID\":[{\"_\":\"098740918237\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}]}],\"PartyName\":[{\"Name\":[{\"_\":\"Ebeneser Scrooge Inc.\"}]}],\"PartyLegalEntity\":[{\"CompanyID\":[{\"_\":\"6411982340\",\"schemeID\":\"UK:CH\",\"schemeAgencyID\":\"ZZZ\"}]}]}],\"Delivery\":[{\"ActualDeliveryDate\":[{\"_\":\"2009-12-15\"}],\"DeliveryLocation\":[{\"ID\":[{\"_\":\"6754238987648\",\"schemeID\":\"GLN\",\"schemeAgencyID\":\"9\"}],\"Address\":[{\"StreetName\":[{\"_\":\"Deliverystreet\"}],\"AdditionalStreetName\":[{\"_\":\"Side door\"}],\"BuildingNumber\":[{\"_\":\"12\"}],\"CityName\":[{\"_\":\"DeliveryCity\"}],\"PostalZone\":[{\"_\":\"523427\"}],\"CountrySubentity\":[{\"_\":\"RegionC\"}],\"Country\":[{\"IdentificationCode\":[{\"_\":\"BE\"}]}]}]}]}],\"PaymentMeans\":[{\"PaymentMeansCode\":[{\"_\":\"31\",\"listID\":\"UN/ECE 4461\"}],\"PaymentDueDate\":[{\"_\":\"2009-12-31\"}],\"PaymentChannelCode\":[{\"_\":\"IBAN\"}],\"PaymentID\":[{\"_\":\"Payref1\"}],\"PayeeFinancialAccount\":[{\"ID\":[{\"_\":\"DK1212341234123412\"}],\"FinancialInstitutionBranch\":[{\"FinancialInstitution\":[{\"ID\":[{\"_\":\"DKDKABCD\"}]}]}]}]}],\"PaymentTerms\":[{\"Note\":[{\"_\":\"Penalty percentage 10% from due date\"}]}],\"AllowanceCharge\":[{\"ChargeIndicator\":[{\"_\":true}],\"AllowanceChargeReason\":[{\"_\":\"Packing cost\"}],\"Amount\":[{\"_\":100,\"currencyID\":\"EUR\"}]},{\"ChargeIndicator\":[{\"_\":false}],\"AllowanceChargeReason\":[{\"_\":\"Promotion discount\"}],\"Amount\":[{\"_\":100,\"currencyID\":\"EUR\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":292.2,\"currencyID\":\"EUR\"}],\"TaxSubtotal\":[{\"TaxableAmount\":[{\"_\":1460.5,\"currencyID\":\"EUR\"}],\"TaxAmount\":[{\"_\":292.1,\"currencyID\":\"EUR\"}],\"TaxCategory\":[{\"ID\":[{\"_\":\"S\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":20}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]},{\"TaxableAmount\":[{\"_\":1,\"currencyID\":\"EUR\"}],\"TaxAmount\":[{\"_\":0.1,\"currencyID\":\"EUR\"}],\"TaxCategory\":[{\"ID\":[{\"_\":\"AA\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":10}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]},{\"TaxableAmount\":[{\"_\":-25,\"currencyID\":\"EUR\"}],\"TaxAmount\":[{\"_\":0,\"currencyID\":\"EUR\"}],\"TaxCategory\":[{\"ID\":[{\"_\":\"E\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":0}],\"TaxExemptionReasonCode\":[{\"_\":\"AAM\",\"listID\":\"CWA 15577\",\"listAgencyID\":\"ZZZ\"}],\"TaxExemptionReason\":[{\"_\":\"Exempt New Means of Transport\"}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]}]}],\"LegalMonetaryTotal\":[{\"LineExtensionAmount\":[{\"_\":1436.5,\"currencyID\":\"EUR\"}],\"TaxExclusiveAmount\":[{\"_\":1436.5,\"currencyID\":\"EUR\"}],\"TaxInclusiveAmount\":[{\"_\":1729,\"currencyID\":\"EUR\"}],\"AllowanceTotalAmount\":[{\"_\":100,\"currencyID\":\"EUR\"}],\"ChargeTotalAmount\":[{\"_\":100,\"currencyID\":\"EUR\"}],\"PrepaidAmount\":[{\"_\":1000,\"currencyID\":\"EUR\"}],\"PayableRoundingAmount\":[{\"_\":0.3,\"currencyID\":\"EUR\"}],\"PayableAmount\":[{\"_\":729,\"currencyID\":\"EUR\"}]}],\"InvoiceLine\":[{\"ID\":[{\"_\":\"1\"}],\"Note\":[{\"_\":\"Scratch on box\"}],\"InvoicedQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}],\"LineExtensionAmount\":[{\"_\":1273,\"currencyID\":\"EUR\"}],\"AccountingCost\":[{\"_\":\"BookingCode001\"}],\"OrderLineReference\":[{\"LineID\":[{\"_\":\"1\"}]}],\"AllowanceCharge\":[{\"ChargeIndicator\":[{\"_\":false}],\"AllowanceChargeReason\":[{\"_\":\"Damage\"}],\"Amount\":[{\"_\":12,\"currencyID\":\"EUR\"}]},{\"ChargeIndicator\":[{\"_\":true}],\"AllowanceChargeReason\":[{\"_\":\"Testing\"}],\"Amount\":[{\"_\":10,\"currencyID\":\"EUR\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":254.6,\"currencyID\":\"EUR\"}]}],\"Item\":[{\"Description\":[{\"_\":\"Processor: Intel Core 2 Duo SU9400 LV (1.4GHz). RAM:\\n"
                + //
                "\\t\\t\\t\\t3MB. Screen 1440x900\",\"languageID\":\"EN\"}],\"Name\":[{\"_\":\"Labtop computer\"}],\"SellersItemIdentification\":[{\"ID\":[{\"_\":\"JB007\"}]}],\"StandardItemIdentification\":[{\"ID\":[{\"_\":\"1234567890124\",\"schemeID\":\"GTIN\",\"schemeAgencyID\":\"9\"}]}],\"CommodityClassification\":[{\"ItemClassificationCode\":[{\"_\":\"12344321\",\"listAgencyID\":\"113\",\"listID\":\"UNSPSC\"}]},{\"ItemClassificationCode\":[{\"_\":\"65434568\",\"listAgencyID\":\"2\",\"listID\":\"CPV\"}]}],\"ClassifiedTaxCategory\":[{\"ID\":[{\"_\":\"S\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":20}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}],\"AdditionalItemProperty\":[{\"Name\":[{\"_\":\"Color\"}],\"Value\":[{\"_\":\"black\"}]}]}],\"Price\":[{\"PriceAmount\":[{\"_\":1273,\"currencyID\":\"EUR\"}],\"BaseQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}],\"AllowanceCharge\":[{\"ChargeIndicator\":[{\"_\":false}],\"AllowanceChargeReason\":[{\"_\":\"Contract\"}],\"MultiplierFactorNumeric\":[{\"_\":0.15}],\"Amount\":[{\"_\":225,\"currencyID\":\"EUR\"}],\"BaseAmount\":[{\"_\":1500,\"currencyID\":\"EUR\"}]}]}]},{\"ID\":[{\"_\":\"2\"}],\"Note\":[{\"_\":\"Cover is slightly damaged.\"}],\"InvoicedQuantity\":[{\"_\":-1,\"unitCode\":\"C62\"}],\"LineExtensionAmount\":[{\"_\":-3.96,\"currencyID\":\"EUR\"}],\"OrderLineReference\":[{\"LineID\":[{\"_\":\"5\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":-0.396,\"currencyID\":\"EUR\"}]}],\"Item\":[{\"Name\":[{\"_\":\"Returned \\\"Advanced computing\\\" book\"}],\"SellersItemIdentification\":[{\"ID\":[{\"_\":\"JB008\"}]}],\"StandardItemIdentification\":[{\"ID\":[{\"_\":\"1234567890125\",\"schemeID\":\"GTIN\",\"schemeAgencyID\":\"9\"}]}],\"CommodityClassification\":[{\"ItemClassificationCode\":[{\"_\":\"32344324\",\"listAgencyID\":\"113\",\"listID\":\"UNSPSC\"}]},{\"ItemClassificationCode\":[{\"_\":\"65434567\",\"listAgencyID\":\"2\",\"listID\":\"CPV\"}]}],\"ClassifiedTaxCategory\":[{\"ID\":[{\"_\":\"AA\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":10}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]}],\"Price\":[{\"PriceAmount\":[{\"_\":3.96,\"currencyID\":\"EUR\"}],\"BaseQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}]}]},{\"ID\":[{\"_\":\"3\"}],\"InvoicedQuantity\":[{\"_\":2,\"unitCode\":\"C62\"}],\"LineExtensionAmount\":[{\"_\":4.96,\"currencyID\":\"EUR\"}],\"OrderLineReference\":[{\"LineID\":[{\"_\":\"3\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":0.496,\"currencyID\":\"EUR\"}]}],\"Item\":[{\"Name\":[{\"_\":\"\\\"Computing for dummies\\\" book\"}],\"SellersItemIdentification\":[{\"ID\":[{\"_\":\"JB009\"}]}],\"StandardItemIdentification\":[{\"ID\":[{\"_\":\"1234567890126\",\"schemeID\":\"GTIN\",\"schemeAgencyID\":\"9\"}]}],\"CommodityClassification\":[{\"ItemClassificationCode\":[{\"_\":\"32344324\",\"listAgencyID\":\"113\",\"listID\":\"UNSPSC\"}]},{\"ItemClassificationCode\":[{\"_\":\"65434566\",\"listAgencyID\":\"2\",\"listID\":\"CPV\"}]}],\"ClassifiedTaxCategory\":[{\"ID\":[{\"_\":\"AA\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":10}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]}],\"Price\":[{\"PriceAmount\":[{\"_\":2.48,\"currencyID\":\"EUR\"}],\"BaseQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}],\"AllowanceCharge\":[{\"ChargeIndicator\":[{\"_\":false}],\"AllowanceChargeReason\":[{\"_\":\"Contract\"}],\"MultiplierFactorNumeric\":[{\"_\":0.1}],\"Amount\":[{\"_\":0.275,\"currencyID\":\"EUR\"}],\"BaseAmount\":[{\"_\":2.75,\"currencyID\":\"EUR\"}]}]}]},{\"ID\":[{\"_\":\"4\"}],\"InvoicedQuantity\":[{\"_\":-1,\"unitCode\":\"C62\"}],\"LineExtensionAmount\":[{\"_\":-25,\"currencyID\":\"EUR\"}],\"OrderLineReference\":[{\"LineID\":[{\"_\":\"2\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":0,\"currencyID\":\"EUR\"}]}],\"Item\":[{\"Name\":[{\"_\":\"Returned IBM 5150 desktop\"}],\"SellersItemIdentification\":[{\"ID\":[{\"_\":\"JB010\"}]}],\"StandardItemIdentification\":[{\"ID\":[{\"_\":\"1234567890127\",\"schemeID\":\"GTIN\",\"schemeAgencyID\":\"9\"}]}],\"CommodityClassification\":[{\"ItemClassificationCode\":[{\"_\":\"12344322\",\"listAgencyID\":\"113\",\"listID\":\"UNSPSC\"}]},{\"ItemClassificationCode\":[{\"_\":\"65434565\",\"listAgencyID\":\"2\",\"listID\":\"CPV\"}]}],\"ClassifiedTaxCategory\":[{\"ID\":[{\"_\":\"E\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":0}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}]}],\"Price\":[{\"PriceAmount\":[{\"_\":25,\"currencyID\":\"EUR\"}],\"BaseQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}]}]},{\"ID\":[{\"_\":\"5\"}],\"InvoicedQuantity\":[{\"_\":250,\"unitCode\":\"C62\"}],\"LineExtensionAmount\":[{\"_\":187.5,\"currencyID\":\"EUR\"}],\"AccountingCost\":[{\"_\":\"BookingCode002\"}],\"OrderLineReference\":[{\"LineID\":[{\"_\":\"4\"}]}],\"TaxTotal\":[{\"TaxAmount\":[{\"_\":37.5,\"currencyID\":\"EUR\"}]}],\"Item\":[{\"Name\":[{\"_\":\"Network cable\"}],\"SellersItemIdentification\":[{\"ID\":[{\"_\":\"JB011\"}]}],\"StandardItemIdentification\":[{\"ID\":[{\"_\":\"1234567890128\",\"schemeID\":\"GTIN\",\"schemeAgencyID\":\"9\"}]}],\"CommodityClassification\":[{\"ItemClassificationCode\":[{\"_\":\"12344325\",\"listAgencyID\":\"113\",\"listID\":\"UNSPSC\"}]},{\"ItemClassificationCode\":[{\"_\":\"65434564\",\"listAgencyID\":\"2\",\"listID\":\"CPV\"}]}],\"ClassifiedTaxCategory\":[{\"ID\":[{\"_\":\"S\",\"schemeID\":\"UN/ECE 5305\",\"schemeAgencyID\":\"6\"}],\"Percent\":[{\"_\":20}],\"TaxScheme\":[{\"ID\":[{\"_\":\"VAT\",\"schemeID\":\"UN/ECE 5153\",\"schemeAgencyID\":\"6\"}]}]}],\"AdditionalItemProperty\":[{\"Name\":[{\"_\":\"Type\"}],\"Value\":[{\"_\":\"Cat5\"}]}]}],\"Price\":[{\"PriceAmount\":[{\"_\":0.75,\"currencyID\":\"EUR\"}],\"BaseQuantity\":[{\"_\":1,\"unitCode\":\"C62\"}]}]}]}]}";

        return formRequest;
    }


    public static ByteArrayInputStream streamToCSV(List<BatchDetailTable> tableList){

        String[] header = {"batchid", "einvoicenumber",  "requestdate", "validationstatus","reason"};
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(header)
                .build();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);


            for (BatchDetailTable b : tableList){
                List<String> data = Arrays.asList(
                        b.getBatchid(),
                        b.getEinvoicenumber(),
                        b.getRequestdate(),
                        b.getValidationstatus(),
                        b.getReason()
                );
                csvPrinter.printRecord(data);
            }


            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
