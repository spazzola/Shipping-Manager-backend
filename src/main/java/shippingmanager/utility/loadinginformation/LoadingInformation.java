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

    private String loadingPlace;

    private String unloadingPlace;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime minLoadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime maxLoadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime minUnloadingDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime maxUnloadingDate;

}
