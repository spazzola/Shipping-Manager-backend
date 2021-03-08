package shippingmanager.company;

import java.util.List;
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

    @PutMapping("/update")
    public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) throws Exception {
        companyService.updateCompany(companyDto);

        return companyDto;
    }

    @DeleteMapping("/deleteCompany")
    public void deleteCompany(@RequestParam("id") Long id) throws Exception {
        companyService.deleteCompany(id);
    }

    @GetMapping("/getCompany")
    public CompanyDto getCompany(@RequestParam("id") Long id) throws Exception {
        Company company = companyService.getCompany(id);

        return companyMapper.toDto(company);
    }

    @GetMapping("/getAll")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyService.getAll();

        return companyMapper.toDto(companies);
    }

}