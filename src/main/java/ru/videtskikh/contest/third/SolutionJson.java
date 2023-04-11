package ru.videtskikh.contest.third;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.videtskikh.contest.first.Solution;

import java.io.*;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SolutionJson implements Solution {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void doTask(String fileInput, String fileOutput) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileInput));
             PrintWriter bw = new PrintWriter(new FileWriter(fileOutput))) {
            String s = br.readLine();
            String[] s1 = s.split(" ");
            int subscribersNumber = Integer.parseInt(s1[0]); //N
            int requestUpdateNumber = Integer.parseInt(s1[1]); //M

            //key - subscriber number
            //value - List<EnumsSet<Fields>> get(0) - triggersSet, get(1) - shipmentsSet
            NavigableMap<Integer, List<EnumSet<Fields>>> triggersShipmentsMapBySubscriberNumber = new TreeMap<>();
            Map<String, Offer> offerMap = new HashMap<>();

            setTriggersShipmentsMap(br, subscribersNumber, triggersShipmentsMapBySubscriberNumber);

            for (int i = 0; i < requestUpdateNumber; i++) {
                String s2 = br.readLine();
                Message message = OBJECT_MAPPER.readValue(s2, Message.class);
                List<Message> messageListOutput = getMessageListForOutput(message, offerMap, triggersShipmentsMapBySubscriberNumber);
                messageListOutput.forEach(x -> {
                    try {
                        bw.println(OBJECT_MAPPER.writeValueAsString(x));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTriggersShipmentsMap(BufferedReader br, int subscribersNumber, NavigableMap<Integer, List<EnumSet<Fields>>> triggersShipmentsMapBySubscriberNumber) throws IOException {
        String s;
        for (int i = 0; i < subscribersNumber; i++) {
            s = br.readLine();
            String[] s2 = s.split(" ");
            EnumSet<Fields> trigerSet = EnumSet.noneOf(Fields.class);
            EnumSet<Fields> shipmentSet = EnumSet.noneOf(Fields.class);
            int triggerFieldNumber = Integer.parseInt(s2[0]);
            int j = 2;
            for (; j < triggerFieldNumber + 2; j++) {
                trigerSet.add(Fields.valueOf(s2[j].toUpperCase()));
            }
            int shipmentFieldNumber = Integer.parseInt(s2[1]);
            for (; j < shipmentFieldNumber + triggerFieldNumber + 2; j++) {
                shipmentSet.add(Fields.valueOf(s2[j].toUpperCase()));
            }
            triggersShipmentsMapBySubscriberNumber.put(i, List.of(trigerSet, shipmentSet));
        }
    }

    private List<Message> getMessageListForOutput(
            Message messageInput,
            Map<String, Offer> offerMap,
            NavigableMap<Integer, List<EnumSet<Fields>>> triggersShipmentsMapBySubscriberNumber) {

        ArrayList<Message> resList = new ArrayList<>();

        String traceId = messageInput.getTraceId(); //notNull
        Offer offerInput = messageInput.getOffer(); //notNull
        String offerId = offerInput.getId();

        Offer offerOld = offerMap.get(offerId);
        Integer priceOld = isNull(offerOld) ? null : offerOld.getPrice();
        Integer stockCountOld = isNull(offerOld) ? null : offerOld.getStockCount();
        PartnerContent partnerContentOld = isNull(offerOld) ? null : offerOld.getPartnerContent();
        String titleOld = isNull(partnerContentOld) ? null : partnerContentOld.getTitle();
        String descriptionOld = isNull(partnerContentOld) ? null : partnerContentOld.getDescription();

        offerMap.merge(offerId, offerInput, this::mergeOffer);

        Offer offerNew = offerMap.get(offerId);
        Integer priceNew = offerNew.getPrice();
        Integer stockCountNew = offerNew.getStockCount();
        PartnerContent partnerContentNew = offerNew.getPartnerContent();
        String titleNew = isNull(partnerContentNew) ? null : partnerContentNew.getTitle();
        String descriptionNew = isNull(partnerContentNew) ? null : partnerContentNew.getDescription();

        EnumSet<Fields> updateFieldSet = EnumSet.noneOf(Fields.class);
        if (!Objects.equals(priceOld, priceNew)) {
            updateFieldSet.add(Fields.PRICE);
        }
        if (!Objects.equals(stockCountOld, stockCountNew)) {
            updateFieldSet.add(Fields.STOCK_COUNT);
        }
        if (!Objects.equals(titleOld, titleNew) || !Objects.equals(descriptionOld, descriptionNew)) {
            updateFieldSet.add(Fields.PARTNER_CONTENT);
        }

        for (Map.Entry<Integer, List<EnumSet<Fields>>> entry : triggersShipmentsMapBySubscriberNumber.entrySet()) {
            EnumSet<Fields> triggersSet = entry.getValue().get(0);
            EnumSet<Fields> shipmentsSet = entry.getValue().get(1);
            EnumSet<Fields> outputSet = EnumSet.copyOf(shipmentsSet);
            outputSet.addAll(intersectionOf(triggersSet, updateFieldSet));

            Offer offerOut = new Offer();
            offerOut.setId(offerId);
            offerOut.setPrice(outputSet.contains(Fields.PRICE) ? priceNew : null);
            offerOut.setStockCount(outputSet.contains(Fields.STOCK_COUNT) ? stockCountNew : null);
            offerOut.setPartnerContent(outputSet.contains(Fields.PARTNER_CONTENT) ?
                    new PartnerContent(titleNew, descriptionNew) : null);

            Message messageOut = new Message(traceId, offerOut);

            if (isMessageOutWithoutNullValue(messageOut)) {
                resList.add(messageOut);
            }
        }

        return resList;
    }

    private boolean isMessageOutWithoutNullValue(Message messageOut) {
        if (messageOut == null) {
            return false;
        }
        Offer offer = messageOut.getOffer();
        if (offer == null) {
            return false;
        }
        Integer price = offer.getPrice();
        Integer stockCount = offer.getStockCount();
        PartnerContent content = offer.getPartnerContent();
        String description = isNull(content) ? null : content.getDescription();
        String title = isNull(content) ? null : content.getTitle();
        return nonNull(price) || nonNull(stockCount) || nonNull(description) || nonNull(title);
    }

    private EnumSet<Fields> intersectionOf(EnumSet<Fields> triggersSet, EnumSet<Fields> updateFieldSet) {
        EnumSet<Fields> set = EnumSet.noneOf(Fields.class);
        for (Fields f1 : triggersSet) {
            if (updateFieldSet.contains(f1)) {
                set.add(f1);
            }
        }
        return set;
    }

    private Offer mergeOffer(Offer ofOld, Offer ofNew) {
        Integer priceNew = ofNew.getPrice();
        Integer stockCountNew = ofNew.getStockCount();
        if (nonNull(priceNew)) {
            ofOld.setPrice(priceNew);
        }
        if (nonNull(stockCountNew)) {
            ofOld.setStockCount(stockCountNew);
        }

        PartnerContent partnerContentNew = ofNew.getPartnerContent();
        String titleNew = nonNull(partnerContentNew) ? partnerContentNew.getTitle() : null;
        String descriptionNew = nonNull(partnerContentNew) ? partnerContentNew.getDescription() : null;
        PartnerContent partnerContentOld = ofOld.getPartnerContent();
        if (isNull(partnerContentOld)) {
            ofOld.setPartnerContent(partnerContentNew);
        } else {
            if (nonNull(titleNew)) {
                partnerContentOld.setTitle(titleNew);
            }
            if (nonNull(descriptionNew)) {
                partnerContentOld.setDescription(descriptionNew);
            }
        }
        return ofOld;
    }

    protected static class Message {

        public Message() {
        }

        public Message(String traceId, Offer offer) {
            this.traceId = traceId;
            this.offer = offer;
        }

        @JsonIgnoreProperties
        @JsonProperty(value = "trace_id", required = true)
        private String traceId;
        @JsonProperty(required = true)
        private Offer offer;

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public Offer getOffer() {
            return offer;
        }

        public void setOffer(Offer offer) {
            this.offer = offer;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "traceId='" + traceId + '\'' +
                    ", offer=" + offer +
                    '}';
        }
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected static class Offer {

        public Offer() {
        }

        @JsonProperty(required = true)
        private String id;
        private Integer price;
        @JsonProperty("stock_count")
        private Integer stockCount;
        @JsonProperty("partner_content")
        private PartnerContent partnerContent;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getStockCount() {
            return stockCount;
        }

        public void setStockCount(Integer stockCount) {
            this.stockCount = stockCount;
        }

        public PartnerContent getPartnerContent() {
            return partnerContent;
        }

        public void setPartnerContent(PartnerContent content) {
            if (nonNull(content) && isNull(content.getTitle()) && isNull(content.getDescription())) {
                this.partnerContent = null;
            } else {
                this.partnerContent = content;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Offer offer = (Offer) o;
            return id.equals(offer.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Offer{" +
                    "id='" + id + '\'' +
                    ", price=" + price +
                    ", stockCount=" + stockCount +
                    ", partnerContent=" + partnerContent +
                    '}';
        }
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected static class PartnerContent {

        public PartnerContent() {
        }

        public PartnerContent(String title, String description) {
            this.title = title;
            this.description = description;
        }

        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "PartnerContent{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    protected enum Fields {
        PRICE("price"),
        STOCK_COUNT("stock_count"),
        PARTNER_CONTENT("partner_content");

        private final String label;

        Fields(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
