package sparta.team6.momo.dto;

import lombok.*;
import sparta.team6.momo.model.MessageType;

@Getter
@Setter
@NoArgsConstructor
public class MapDto {
    private Long planId;
    private String sender;
    private String lat;
    private String lng;
    private MessageType type;
    private String destLat;
    private String destLng;

    @Builder
    public MapDto(Long planId, String sender, String lat, String lng, String destLat, String destLng) {
        this.planId = planId;
        this.sender = sender;
        this.lat = lat;
        this.lng = lng;
        this.destLat = destLat;
        this.destLng = destLng;
    }


    public static MapDto from(EnterDto enterDto) {
        return MapDto.builder()
                .planId(enterDto.getPlanId())
                .sender(enterDto.getSender())
                .lat(enterDto.getLat())
                .lng(enterDto.getLng())
                .build();
    }
}
