package sparta.team6.momo.dto;

import lombok.*;
import sparta.team6.momo.model.MessageType;

@Getter
@Setter
@NoArgsConstructor
public class MapDto {
    private Long planId;
    private String sender;
    private Double lat;
    private Double lng;
    private MessageType type;
    private Double destLat;
    private Double destLng;

    @Builder
    public MapDto(Long planId, String sender, Double lat, Double lng, Double destLat, Double destLng, MessageType type) {
        this.planId = planId;
        this.sender = sender;
        this.lat = lat;
        this.lng = lng;
        this.destLat = destLat;
        this.destLng = destLng;
        this.type = type;
    }


    public static MapDto from(EnterDto enterDto) {
        return MapDto.builder()
                .planId(enterDto.getPlanId())
                .sender(enterDto.getSender())
                .lat(enterDto.getLat())
                .lng(enterDto.getLng())
                .type(MessageType.DEST)
                .build();
    }
}
