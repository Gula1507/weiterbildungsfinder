package de.neuefische.backend.api.service;

import de.neuefische.backend.api.dto.*;
import de.neuefische.backend.api.exception.ApiResponseException;
import de.neuefische.backend.organization.IdService;
import de.neuefische.backend.organization.model.Course;
import de.neuefische.backend.organization.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArbeitsagenturApiService {

    private final RestClient restClient;
    private final IdService idService;
    private static final Logger logger = LoggerFactory.getLogger(ArbeitsagenturApiService.class);

    public ArbeitsagenturApiService(RestClient.Builder builder, IdService idService) {
        this.restClient =
                builder.baseUrl("https://rest.arbeitsagentur.de/infosysbub/wbsuche/pc/v2/bildungsangebot").build();
        this.idService = idService;
    }

    public List<Organization> loadAllOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        List <Course> allCourses=new ArrayList<>();

        String urlPage = "?page=0&size=20";
        while (urlPage != null) {
            try {
                ApiResponse response =
                        restClient.get().uri(urlPage).header("X-API-Key", "infosysbub-wbsuche").retrieve().body(ApiResponse.class);

                if (response == null || response.responseContent() == null || response.responseContent().details() == null) {
                    logger.error("Fehler: responseContent ist null für die Seite {}", urlPage);
                    break;
                }

                List<ApiResponseDetails> details = response.responseContent().details();
                List <Course> courses=new ArrayList<>();
                List<ApiResponseOrganization> apiResponseOrganizations =
                        details.stream().map(ApiResponseDetails::courseOffer).map(ApiResponseCourseOffer::apiResponseOrganization).distinct().toList();

                for(ApiResponseDetails apiResponseDetails:details) {
                    courses.add(new Course(idService.generateRandomId(),
                            apiResponseDetails.courseOffer().courseId(),
                            apiResponseDetails.courseOffer().courseName(),
                            apiResponseDetails.courseOffer().courseContent(),
                            apiResponseDetails.courseOffer().courseDegree(),
                            apiResponseDetails.courseOffer().educationVoucher(),
                            new CourseType(apiResponseDetails.courseOffer().courseType().courseTypeName()),
                            apiResponseDetails.courseOffer().apiResponseOrganization().id()));}
                allCourses.addAll(courses);
                organizations.addAll(convertApiOrganizationsToOrganizations(apiResponseOrganizations));

                urlPage = getNextPageUrl(response);
            } catch (Exception e) {
                throw new ApiResponseException();
            }
        }
        List<Course> uniqueCourses=allCourses.stream().distinct().toList();
        organizations=addCoursesToOrganizations(organizations,uniqueCourses);

        return organizations.stream().distinct().toList();
    }

       public List<Organization> convertApiOrganizationsToOrganizations(List<ApiResponseOrganization> apiResponseOrganizations)
    {        return apiResponseOrganizations.stream().map(a -> new Organization(
                idService.generateRandomId(),
                a.id(),
                a.name(),
                a.homepage(),
                a.email(),
                a.address().streetAndHomeNumber() + ", " + a.address().addressDetails().postalCode() + " "
                + a.address().addressDetails().city(),
                new ArrayList<>(), 0.0,new ArrayList<>())).toList();
    }

    public List<Organization> addCoursesToOrganizations(List<Organization> organizations, List<Course> courses) {
        List<Organization> updatedOrganizations = new ArrayList<>();
        for (Organization organization : organizations) {
            List<Course> updatedCourses = new ArrayList<>(organization.courses());
            for (Course course : courses) {
                if (organization.apiId().equals(course.apiOrganizationId())) {
                    updatedCourses.add(course);

                }
            }
            updatedOrganizations.add(new Organization(
                    organization.id(),
                    organization.apiId(),
                    organization.name(),
                    organization.homepage(),
                    organization.email(),
                    organization.address(),
                    organization.reviews(),
                    organization.averageRating(),
                    updatedCourses
            ));
        }
        return updatedOrganizations;
    }

    public String getNextPageUrl(ApiResponse apiResponse) {
        if (apiResponse != null && apiResponse.page() != null && apiResponse.page().number() < apiResponse.page().totalPages()-1) {
            return "?page=" + (apiResponse.page().number() + 1) + "&size=20";
        }
        return null;
    }
}
