package ru.videtskikh.contest.third;

import lombok.Builder;
import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Random;

import static ru.videtskikh.contest.third.SolutionJson.OBJECT_MAPPER;

public class TestingDataCreator {

    private static final Random RANDOM = new Random();

    private static int traceCount = 1;


    public static void main(String[] args) {
        CreatorSettings settings1 = CreatorSettings.builder()
                .operationsNumber(10_000)
                .subsribersNumber(5)
                .maxOffersNumber(2).build();
        createTestFile("src/main/resources/contest3/example2-generated.txt", settings1);
    }

    public static void createTestFile(String fileName, CreatorSettings settings) {
        try (PrintWriter pr = new PrintWriter(new FileWriter(fileName))) {
            int n = settings.subsribersNumber;
            int m = settings.operationsNumber;
            int maxOffersNumber = settings.maxOffersNumber;
            pr.println(n + " " + m);
            for (int i = 0; i < n; i++) {
                EnumSet<SolutionJson.Fields> triggersSet = getRandomTriggerShipmentSet();
                EnumSet<SolutionJson.Fields> shipmentsSet = getRandomTriggerShipmentSet(triggersSet);
                while (triggersSet.isEmpty() && shipmentsSet.isEmpty()) {
                    triggersSet = getRandomTriggerShipmentSet();
                    shipmentsSet = getRandomTriggerShipmentSet(triggersSet);
                }
                StringBuilder line = new StringBuilder(triggersSet.size() + " " + shipmentsSet.size() + " ");
                for (SolutionJson.Fields field : triggersSet) {
                    line.append(field.toString().toLowerCase()).append(" ");
                }
                for (SolutionJson.Fields field : shipmentsSet) {
                    line.append(field.toString().toLowerCase()).append(" ");
                }
                line = new StringBuilder(line.toString().trim());
                pr.println(line);


            }
            for (int i = 0; i < m; i++) {
                SolutionJson.Message randomMessage = getRandomMessage(maxOffersNumber);
                pr.println(OBJECT_MAPPER.writeValueAsString(randomMessage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static EnumSet<SolutionJson.Fields> getRandomTriggerShipmentSet(EnumSet<SolutionJson.Fields> excludeSet) {
        EnumSet<SolutionJson.Fields> set = getRandomTriggerShipmentSet();
        set.removeAll(excludeSet);
        return set;
    }

    private static EnumSet<SolutionJson.Fields> getRandomTriggerShipmentSet() {
        int random = getRandom(0, 3);
        EnumSet<SolutionJson.Fields> es = EnumSet.noneOf(SolutionJson.Fields.class);
        switch (random) {
            case 1 -> es.add(SolutionJson.Fields.values()[getRandom(0, 2)]);
            case 2 -> {
                while (es.size() < 2) {
                    es.add(SolutionJson.Fields.values()[getRandom(0, 2)]);
                }
            }
            case 3 -> es = EnumSet.allOf(SolutionJson.Fields.class);
        }
        return es;
    }

    private static SolutionJson.Message getRandomMessage(int maxOffersNumber) {
        SolutionJson.Message message = new SolutionJson.Message();

        message.setTraceId(String.valueOf(traceCount++));
        SolutionJson.Offer offer = new SolutionJson.Offer();
        offer.setId(String.valueOf(getRandom(1, maxOffersNumber)));
        SolutionJson.PartnerContent partnerContent = new SolutionJson.PartnerContent();
        switch (getRandom(1, 4)) {
            case 1 -> offer.setPrice(getRandom(1, 100000));
            case 2 -> offer.setStockCount(getRandom(1, 100000));
            case 3 -> {
                partnerContent.setTitle("title_" + getRandom(1, 100));
                offer.setPartnerContent(partnerContent);
            }
            case 4 -> {
                partnerContent.setDescription("description_" + getRandom(1, 100));
                offer.setPartnerContent(partnerContent);
            }
        }

        message.setOffer(offer);
        return message;
    }


    public static int getRandom(int min, int max) {
        return RANDOM.nextInt(max + 1 - min) + min;
    }


    @Builder
    @Getter
    private static class CreatorSettings {
        private int subsribersNumber;
        private int operationsNumber;
        private int maxOffersNumber;
    }
}
