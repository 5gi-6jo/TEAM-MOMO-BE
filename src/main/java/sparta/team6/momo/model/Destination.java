package sparta.team6.momo.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dest_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column
    private String address;

    @Column
    private Double lat;

    @Column
    private Double lng;

    @Builder
    public Destination(Plan plan, String address, Double lat, Double lng) {
        this.plan = plan;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

}
