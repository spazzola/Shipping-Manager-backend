package shippingmanager.company;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CompanyController {

    private CompanyService companyService;
    private CompanyMapper companyMapper;
    private Logger logger;


    @PostMapping("/create")
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        logger.info("Dodawanie firmy: " + companyDto);
        Company company = companyService.createCompany(companyDto);

        return companyMapper.toDto(company);
    }

    @PutMapping("/update")
    public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) throws Exception {
        logger.info("Aktualizowanie firmy: " + companyDto);
        companyService.updateCompany(companyDto);

        return companyDto;
    }

    @DeleteMapping("/deleteCompany")
    public void deleteCompany(@RequestParam("id") Long id) throws Exception {
        logger.info("Usuwanie firmy o id: " + id);
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