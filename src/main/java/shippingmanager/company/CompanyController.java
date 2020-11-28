package shippingmanager.company;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {

    private CompanyService companyService;
    private CompanyMapper companyMapper;


    @PostMapping("/create")
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        Company company = companyService.createCompany(companyDto);

        return companyMapper.toDto(company);
    }

}
