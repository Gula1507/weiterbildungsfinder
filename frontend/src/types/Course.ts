import {CourseType} from "./CourseType.ts";

export type Course= {
    id:string,
    apiCourseId: number,
    courseName:string,
    courseContent:string,
    courseDegree:string,
    educationVoucher:string,
    courseType: CourseType,
    apiOrganizationId: number;
}