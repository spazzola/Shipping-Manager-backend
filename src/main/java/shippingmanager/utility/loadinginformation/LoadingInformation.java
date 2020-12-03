package shippingmanager.utility.loadinginformation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import shippingmanager.company.Company;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loading_place_fk")
    private Company loadingPlace;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unloading_place_fk")
    private Company unloadingPlace;

    @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime minLoadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime maxLoadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime minUnloadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime maxUnloadingDate;

}
