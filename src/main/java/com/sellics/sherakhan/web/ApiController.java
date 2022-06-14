package com.sellics.sherakhan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    private final JdbcTemplate jdbcTemplate;

    public ApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*
     * Get consolidated report of products according to serialNumber(asin) and/or start date and end date combination.
     *
     * curl -i -X GET --location "http://localhost:7171/api/ranks/products"
     * curl -i -X GET --location "http://localhost:7171/api/ranks/products?serialNumber=B09LS4PB8P"
     * curl -i -X GET --location "http://localhost:7171/api/ranks/products?startDate=1635988126&endDate=1637199767&serialNumber=B09LS4PB8P"
     *
     */
    @RequestMapping(value = "/ranks/products", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> aggregateRankOfProducts(
            @RequestParam(required = false, value = "serialNumber") final String serialNumber,
            @RequestParam(required = false, value = "startDate") final String startDate,
            @RequestParam(required = false, value = "endDate") final String endDate) {

        LOGGER.info("Search for Product Rank using Params- Serial Number {}, Start Date {}, End Date {}",
                serialNumber, startDate, endDate);
        List<Object> args = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as number_of_ranks, serial_number from product_ranks ");
        if (StringUtils.hasText(serialNumber)
                && !StringUtils.hasText(startDate)
                && !StringUtils.hasText(endDate)) {
            query.append("where serial_number = ?");
            args.add(serialNumber);
        }

        if (StringUtils.hasText(serialNumber)
                && StringUtils.hasText(startDate)
                && StringUtils.hasText(endDate)) {
            query.append("where serial_number = ? and event_date >=? and event_date < ? ");
            args.add(serialNumber);
            args.add(startDate);
            args.add(endDate);
        }

        if (!StringUtils.hasText(serialNumber)
                && StringUtils.hasText(startDate)
                && StringUtils.hasText(endDate)) {
            query.append("where event_date >=? and event_date < ? ");
            args.add(startDate);
            args.add(endDate);
        }

        query.append("group by serial_number ");
        query.append("order by serial_number ");

        LOGGER.trace("Trigger search for products rank query- {} with args {}", query, args.toArray(new Object[]{}));

        List<Map<String, Object>> data = jdbcTemplate.queryForList(query.toString(), args.toArray(new Object[]{}));

        return ResponseEntity.ok(data);
    }


    /*
     * Get consolidated report of products according to keywords and/or start date and end date combination.
     *
     * curl -i -X GET --location "http://localhost:7171/api/keywords/products?keyword=2012"
     * curl -i -X GET --location "http://localhost:7171/api/keywords/products?keyword=2012&startDate=1637187600&endDate=1637199767"
     *
     */
    @RequestMapping(value = "/keywords/products", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> allProductsByKeywords(
            @RequestParam(required = true, value = "keyword") final String keyword,
            @RequestParam(required = false, value = "startDate") final String startDate,
            @RequestParam(required = false, value = "endDate") final String endDate) {

        LOGGER.info("Search Product by keywords - Keyword {}", keyword);
        String metadata = "%" + keyword + "%";
        List<Object> args = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("select count(*) as number_of_ranks, serial_number from product_ranks where keywords like ? ");
        args.add(metadata);

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            query.append("and event_date >= ? and event_date < ? ");
            args.add(startDate);
            args.add(endDate);
        }

        query.append("group by serial_number ");
        query.append("order by serial_number ");

        LOGGER.info("Trigger search for products by keywords query- {} with args {}", query, args.toArray(new Object[]{}));

        List<Map<String, Object>> data = jdbcTemplate.queryForList(query.toString(), args.toArray(new Object[]{}));
        return ResponseEntity.ok(data);
    }
}
