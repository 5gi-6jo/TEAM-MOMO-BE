package sparta.team6.momo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sparta.team6.momo.model.Destination;

@Getter
@NoArgsConstructor
public class DestinationDto {

    private String address;
    private Double lat;
    private Double lng;

    public DestinationDto(Destination destination) {
        this.address = destination.getAddress();
        this.lat = destination.getLat();
        this.lng = destination.getLng();
    }
}
