package sparta.team6.momo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MapDto {
    private String planId;
    private String sender;
    private String lat;
    private String lng;
    private String destLat;
    private String destLng;
}
