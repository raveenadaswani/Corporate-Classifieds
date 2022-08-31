package com.cts.pointsmicroservice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

class OfferTest {
    @Test
    void testConstructor() {
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date openDate = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date engagedDate = Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Offer actualOffer = new Offer(1, "Name", "The characteristics of someone or something", "Category", openDate,
                engagedDate, Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()), 1);
        actualOffer.setCategory("Category");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant());
        actualOffer.setClosedDate(fromResult);
        actualOffer.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult1 = Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant());
        actualOffer.setEngagedDate(fromResult1);
        actualOffer.setId(1);
        actualOffer.setLikes(1);
        actualOffer.setName("Name");
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult2 = Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant());
        actualOffer.setOpenDate(fromResult2);
        assertEquals("Category", actualOffer.getCategory());
        assertSame(fromResult, actualOffer.getClosedDate());
        assertEquals("The characteristics of someone or something", actualOffer.getDescription());
        assertSame(fromResult1, actualOffer.getEngagedDate());
        assertEquals(1, actualOffer.getId());
        assertEquals(1, actualOffer.getLikes());
        assertEquals("Name", actualOffer.getName());
        assertSame(fromResult2, actualOffer.getOpenDate());
    }
}

