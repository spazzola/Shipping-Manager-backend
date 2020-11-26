package shippingmanager.utility.loadinginformation;

import lombok.*;
import org.joda.time.DateTime;
import shippingmanager.company.Company;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loading_information")
public class LoadingInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loading_information_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loading_place_fk")
    private Company loadingPlace;

    @ManyToOne
    @JoinColumn(name = "unloading_place_fk")
    private Company unloadingPlace;

    private DateTime minLoadingDate;

    private DateTime maxLoadingDate;

    private DateTime minUnloadingDate;

    private DateTime maxUnloadingDate;

}
